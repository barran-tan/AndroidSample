package com.test.plugins.dataclass;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class OptDataClassClassVisitor extends ClassNode {

    public static AtomicLong cost = new AtomicLong();

    private static final String NEW_SUPER_CLASS = "com/barran/sample/asmtest/dataclass/AbsDataClass";

    private static final String INIT_METHOD = "<init>";

    private boolean isChecked;

    private boolean needInject;

    private ClassVisitor classVisitor;

    public OptDataClassClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM7);
        classVisitor = cv;
    }

//    @Override
//    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        super.visit(version, access, name, signature, superName, interfaces);
//        // 未开始遍历前，拿不到fields及methods
////        checkInject("visit");
//    }

    private void checkInject(String log) {
        if (isChecked) {
            return;
        }
        boolean isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        if (!isInterface) {
//            boolean testLog = name.contains("DataClass");
            boolean testLog = false;
            if (testLog) {
                System.out.println("----" + log + " class=" + name + " method size " + methods.size() + ", field size " + fields.size());
            }
            if (methods.size() == 0 || fields.size() == 0 || !"java/lang/Object".equals(superName)) {
                return;
            }

            boolean hasCopy = false;
            boolean hasCopyDefault = false;
            int componentMethodCount = 0;
            int matchCount = 0;
            for (MethodNode method : methods) {
                if ("copy".equals(method.name)) {
                    hasCopy = true;
                } else if ("copy$default".equals(method.name)) {
                    if ((method.access & Opcodes.ACC_STATIC) != 0) {
                        hasCopyDefault = true;
                    } else {
                        System.out.println("----copy$default acc_flag: " + method.access + " isStatic " + (method.access & Opcodes.ACC_STATIC));
                    }
                } else if (method.name.matches("component\\d+")) {
                    componentMethodCount++;
                    int index = Integer.parseInt(method.name.substring(9)) - 1;
                    String subDesc = method.desc.substring(2);
                    if (fields.get(index).desc.equals(subDesc)) {
                        matchCount++;
                    } else {
                        System.out.printf("----component method and field not match: index=%d field_desc=%s method desc=%s sub_desc=%s\n",
                                index, fields.get(index).desc, method.desc, subDesc);
                    }
                } else {
                    if (testLog) {
                        System.out.println("----method: " + method.name + "  " + method.desc);
                    }
                }
            }

            needInject = hasCopy && hasCopyDefault && (componentMethodCount == matchCount);
            if (needInject) {
                System.out.println("----start inject opt_data_class of " + name);
            } else {
                if (hasCopy || hasCopyDefault || matchCount > 0) {
                    System.out.printf("----no need inject  hasCopy=%s hasCopyDefault=%s componentMethodCount=%d matchCount=%d\n"
                            , hasCopy, hasCopyDefault, componentMethodCount, matchCount);
                }
            }
            isChecked = true;
        }
    }

    @Override
    public void visitEnd() {
        checkInject("visitEnd");
        if (needInject) {
            long start = System.currentTimeMillis();

            changeSuperClass();

            optMethod();

            addGetObjectsMethod();

            cost.addAndGet(System.currentTimeMillis() - start);
        }
        if (classVisitor != null) {
            accept(classVisitor);
        }
    }

    private void changeSuperClass() {
        superName = NEW_SUPER_CLASS;
        boolean suc = false;
        for (MethodNode method : methods) {
            // update call super of <init>
            if (INIT_METHOD.equals(method.name)) {
                int size = method.instructions.size();
                MethodInsnNode targetCallSuper = null;
                for (int i = 0; i < size; i++) {
                    AbstractInsnNode insnNode = method.instructions.get(i);
                    if (!(insnNode instanceof MethodInsnNode)) {
                        continue;
                    }
                    MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                    if (insnNode.getOpcode() == Opcodes.INVOKESPECIAL
                            && "java/lang/Object".equals(methodInsnNode.owner)
                            && INIT_METHOD.equals(methodInsnNode.name)
                            && "()V".equals(methodInsnNode.desc)) {
                        // check pre insn is var of this
                        // or some new Object in <init> will be changed
                        if (i > 0) {
                            AbstractInsnNode pre = method.instructions.get(i - 1);
                            if (pre instanceof VarInsnNode && pre.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) pre).var == 0) {
                                targetCallSuper = methodInsnNode;
                            } else {
                                System.out.println("----ignore call Object.<init>()V");
                            }
                        } else {
                            System.out.println("----??maybe illegal state cause call super <init> without call var of this");
                        }

                        break;
                    }
                }

                if (targetCallSuper != null) {
                    MethodInsnNode newNode = new MethodInsnNode(Opcodes.INVOKESPECIAL,
                            NEW_SUPER_CLASS, INIT_METHOD,
                            "()V", false);
                    method.instructions.set(targetCallSuper, newNode);
                    System.out.println("----replace call super <init> of " + name);
                    suc = true;
                }
            }
        }

        if (!suc) {
            System.out.println("----!!illegal state cause call super <init> not found");
        }
    }

    private void optMethod() {
        ArrayList<MethodNode> removeList = new ArrayList<>();
        for (MethodNode method : methods) {
            if (("equals".equals(method.name) && "(Ljava/lang/Object;)Z".equals(method.desc))
                    || ("hashCode".equals(method.name) && "()I".equals(method.desc))
                    || ("toString".equals(method.name) && "()Ljava/lang/String;".equals(method.desc))) {
                removeList.add(method);
            }
        }
        int removeSize = removeList.size();
        if (removeSize == 3) {
            methods.removeAll(removeList);
            System.out.println("----remove methods count " + removeSize);
        } else {
            System.out.println("----!!illegal state cause remove methods size is " + removeSize);
        }
    }

    private void addGetObjectsMethod() {
        MethodNode methodVisitor = (MethodNode) visitMethod(Opcodes.ACC_PUBLIC, "getObjects", "()[Ljava/lang/Object;", null, null);

        ArrayList<FieldNode> fieldList = getFieldList();
        int fieldSize = fieldList.size();
        System.out.println("----getObject field size " + fieldSize);
        // new object array
        visitIntInsn(methodVisitor, fieldSize);
        methodVisitor.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 1);
        // fill array with fields
        for (int i = 0; i < fieldSize; i++) {
            addFiled(methodVisitor, fieldList.get(i), i);
        }
        // return array
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        methodVisitor.visitInsn(Opcodes.ARETURN);
        methodVisitor.visitMaxs(4, 2);
        methodVisitor.visitEnd();
    }

    // ignore some field by compiler and return the origin field list
    private ArrayList<FieldNode> getFieldList(){
        ArrayList<FieldNode> list = new ArrayList<>();
        for (FieldNode field : fields) {
            if ((field.access & Opcodes.ACC_STATIC) != 0 || (field.access & Opcodes.ACC_SYNTHETIC) != 0) {
                System.out.println("----ignore field=" + field.name + " static=" + ((field.access & Opcodes.ACC_STATIC) != 0)
                        + " synthetic=" + ((field.access & Opcodes.ACC_SYNTHETIC) != 0));
                continue;
            }
            list.add(field);
        }
        return list;
    }

    private void visitIntInsn(MethodVisitor methodVisitor, int value) {
        if (value > 5) {
            methodVisitor.visitIntInsn(Opcodes.BIPUSH, value);
        } else {
            switch (value) {
                case 0:
                    methodVisitor.visitInsn(Opcodes.ICONST_0);
                    break;
                case 1:
                    methodVisitor.visitInsn(Opcodes.ICONST_1);
                    break;
                case 2:
                    methodVisitor.visitInsn(Opcodes.ICONST_2);
                    break;
                case 3:
                    methodVisitor.visitInsn(Opcodes.ICONST_3);
                    break;
                case 4:
                    methodVisitor.visitInsn(Opcodes.ICONST_4);
                    break;
                case 5:
                    methodVisitor.visitInsn(Opcodes.ICONST_5);
                    break;
                default:
                    throw new IllegalStateException(String.format("illegal int=%d in %s", value, name));
            }
        }
    }

    private void addFiled(MethodVisitor methodVisitor, FieldNode field, int index) {
        // load array
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        visitIntInsn(methodVisitor, index);

        String type = field.desc;

        // get filed
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, name, field.name, type);
        // convert basic type to Object
        if (type.length() == 1) {
            switch (type) {
                case "B":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                    break;
                case "C":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                    break;
                case "D":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                    break;
                case "F":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                    break;
                case "I":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                    break;
                case "J":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                    break;
                case "S":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                    break;
                case "Z":
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                    break;
                default:
                    throw new IllegalStateException("error: unhandled type " + type);
            }
        }
        methodVisitor.visitInsn(Opcodes.AASTORE);
    }
}
