package com.test.plugins.newthread;

import com.android.tools.r8.graph.V;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public class NewThreadClassVisitor extends ClassNode {

    private static final String TAG = "--newThread ";

    private static final String THRED_CLASS_NAME = "java/lang/Thread";

    private static final String THRED_TYPE_DESC = "Ljava/lang/Thread;";

    private static final String START_METHOD_NAME = "start";

    private static final String RUNNABLE_CLASS_NAME = "java/lang/Runnable";

    private static final String CONSTRUCTOR_METHOD = "<init>";

    private ClassVisitor classVisitor;

    private static final String HANDLER_CLASS_NAME = "com/barran/example/asmtest/AsmHandler";
    private static final String HANDLER_METHOD_NAME = "handleNewThread";
    private static final String HANDLER_METHOD_DESC = "(Ljava/lang/Runnable;)V";

    public NewThreadClassVisitor(ClassVisitor visitor) {
        super(Opcodes.ASM7);
        classVisitor = visitor;
    }

    @Override
    public void visitEnd() {

        methods.forEach(new Consumer<MethodNode>() {
            @Override
            public void accept(MethodNode methodNode) {
                handleMethodInsn(methodNode);
            }
        });

        if (classVisitor != null) {
            accept(classVisitor);
        }
    }

    private void handleMethodInsn(MethodNode methodNode) {
        InsnList insnList = methodNode.instructions;
        ListIterator<AbstractInsnNode> iterator = insnList.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insnNode = iterator.next();
            if (insnNode instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) insnNode;
                if (checkIsTargetInvoke(methodInsn)) {

                    System.out.println(TAG + "handleMethod class: " + name + ",method=" + methodNode.name + ", desc=" + methodNode.desc);

                    // for test
//                    printInsnList(methodNode);

                    removeUnusedInsn(methodNode, insnList, methodInsn);

                    MethodInsnNode newMethodInsn = new MethodInsnNode(Opcodes.INVOKESTATIC,
                            HANDLER_CLASS_NAME, HANDLER_METHOD_NAME, HANDLER_METHOD_DESC);
                    insnList.set(methodInsn, newMethodInsn);

                    System.out.println(TAG + "after handle");
                    for (AbstractInsnNode node : methodNode.instructions) {
                        System.out.println("   insn:  type=" + node.getType() + ",opCode=" + node.getOpcode() + ", toStr=" + node);
                    }
                }
            }
        }
    }

    // 仅针对Thread调用包含Runnable参数的构造方法
    private boolean checkIsTargetInvoke(MethodInsnNode methodInsn) {
        int opcode = methodInsn.getOpcode();
        boolean isTarget = (opcode == Opcodes.INVOKESPECIAL && THRED_CLASS_NAME.equals(methodInsn.owner)
                && CONSTRUCTOR_METHOD.equals(methodInsn.name));
        if (isTarget) {
            AbstractInsnNode next = methodInsn.getNext();
            if (next instanceof FieldInsnNode && next.getOpcode() == Opcodes.PUTFIELD) {
                FieldInsnNode fieldNode = (FieldInsnNode) next;
                if (THRED_TYPE_DESC.equals(fieldNode.desc)) {
                    System.out.println(TAG + "checkIsTargetInvoke ignore: next is PUTFIELD: name=" + fieldNode.name + ",desc=" + fieldNode.desc);
                    return false;
                }
            } else {
                // 仅构造Thread但是未调用start,例如只是创建对象并返回
                if (next.getOpcode() == Opcodes.ARETURN) {
                    return false;
                }
                boolean callStart = false;
                while (next != null) {
                    if (next.getOpcode() >= Opcodes.IRETURN && next.getOpcode() <= Opcodes.RETURN) {
                        break;
                    }
                    if (next instanceof MethodInsnNode) {
                        MethodInsnNode node = (MethodInsnNode) next;
                        if (THRED_CLASS_NAME.equals(node.owner) && START_METHOD_NAME.equals(node.name)) {
                            callStart = true;
                            break;
                        }
                    }
                    next = next.getNext();
                }
                if (!callStart) {
                    System.out.println(TAG + "checkIsTargetInvoke ignore: not call start");
                    return false;
                }
            }
            String desc = methodInsn.desc;
            System.out.println("\n");
            if (desc.contains(RUNNABLE_CLASS_NAME)) {
                System.out.println(TAG + "checkIsTargetInvoke desc " + desc);
                return true;
            } else {
                System.out.println(TAG + "checkIsTargetInvoke ignore: no Runnable param");
                return false;
            }
        }
        return false;
    }

    // 找出当前指令前后的无效指令并删除
    private void removeUnusedInsn(MethodNode methodNode, InsnList list, MethodInsnNode anchor) {
        List<AbstractInsnNode> removeList = new ArrayList<>();

        removeList.addAll(findConstructParamInsn(list,anchor));
        int size = removeList.size();
        System.out.println(TAG + "remove pre insn size " + size);

        removeList.addAll(findLocalVarUsageInsn(list, anchor));
        System.out.println(TAG + "remove next insn size " + (removeList.size() - size));

        for (AbstractInsnNode node : removeList) {
            list.remove(node);
//            System.out.println("    remove: type=" + node.getType() + ", op=" + node.getOpcode() + ",insn=" + node);
        }
    }

    // 找出当前指令使用的aload参数指令
    private List<AbstractInsnNode> findConstructParamInsn(InsnList list, MethodInsnNode anchor) {
        List<AbstractInsnNode> removeList = new ArrayList<>();
        List<String> params = parseParamFromDesc(anchor.desc);
        int size = params.size();
        if (size > 0) {
            // 删除构造相关无效指令，如NEW，DUP
            System.out.println(TAG + "pre:remove insn");
            AbstractInsnNode pre = anchor.getPrevious();
            while (pre != null) {

                int opcode = pre.getOpcode();
                int type = pre.getType();
//                System.out.println("    pre: op=" + opcode + ", type=" + type + ",insn=" + pre);

                // 起始指令
                if (opcode == Opcodes.NEW) {
                    if (pre instanceof TypeInsnNode && THRED_CLASS_NAME.equals(((TypeInsnNode) pre).desc)) {
                        removeList.add(pre);
                        AbstractInsnNode next = pre.getNext();
                        if (next != null && next.getOpcode() == Opcodes.DUP) {
                            removeList.add(next);
                        }
                        break;
                    }
                } else if (type == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldc = (LdcInsnNode) pre;
                    if (checkLDCParam(params, ldc.cst)) {
                        removeList.add(pre);
                    }
                }

                pre = pre.getPrevious();
            }

        } else {
            System.out.println(TAG + "pre:constructor use no params desc= " + anchor.desc);
        }
        return removeList;
    }

    // 检查ldc对象是否属于参数列表
    private boolean checkLDCParam(List<String> params, Object obj) {
        boolean match = false;
        for (String param : params) {
            switch (param) {
                case "B":
                case "C":
                case "I":
                case "S":
                    if (obj instanceof Integer) {
                        match = true;
                    }
                    break;
                case "D":
                    if (obj instanceof Double) {
                        match = true;
                    }
                    break;
                case "F":
                    if (obj instanceof Float) {
                        match = true;
                    }
                    break;
                case "J":
                    if (obj instanceof Long) {
                        match = true;
                    }
                    break;
                case "Z":
                    if (obj instanceof Boolean) {
                        match = true;
                    }
                    break;
                case "V":
                    if (obj instanceof Void) {
                        match = true;
                    }
                    break;
                default:
                    if (param.contains("String") && obj instanceof String) {
                        match = true;
                    }
                    break;
            }
        }
        return match;
    }

    private List<AbstractInsnNode> findLocalVarUsageInsn(InsnList list, MethodInsnNode anchor){
        List<AbstractInsnNode> removeList = new ArrayList<>();
        // 删除后面无效指令，如后续的调用
        AbstractInsnNode next = anchor.getNext();
        if (next == null) {
            return removeList;
        }
        System.out.println(TAG + "next:remove insn");
//        System.out.println("    next: op=" + next.getOpcode() + ", type=" + next.getType() + ",insn=" + next);
        if (next instanceof MethodInsnNode) {
            // new xxx().start()
            MethodInsnNode method = (MethodInsnNode) next;
            if (method.getOpcode() == Opcodes.INVOKEVIRTUAL && THRED_CLASS_NAME.equals(method.owner)) {
                removeList.add(method);
            }
        } else if (next instanceof VarInsnNode && next.getOpcode() == Opcodes.ASTORE) {
            // xxx = new XXX
            // xxx.start()
            removeList.add(next);
            VarInsnNode var = (VarInsnNode) next;
            int operand = var.var;

            next = next.getNext();
            boolean loadVar = false;
            while (next != null) {

//                System.out.println("    next: op=" + next.getOpcode() + ",insn=" + next);
                // 删除后续调用
                if (next instanceof VarInsnNode && next.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) next).var == operand) {
                    loadVar = true;
                } else if (loadVar && next instanceof MethodInsnNode && THRED_CLASS_NAME.equals(((MethodInsnNode) next).owner)) {
                    AbstractInsnNode pre = next;
                    while (pre != null) {
                        removeList.add(pre);
                        if (pre.getType() == AbstractInsnNode.LABEL) {
                            break;
                        }
                        pre = pre.getPrevious();
                    }
                    loadVar = false;
                }

                next = next.getNext();
            }
        } else {
            // new xxx(new yyy()).start()
            if (next.getType() == AbstractInsnNode.LABEL) {
                ArrayList<AbstractInsnNode> tempList = new ArrayList<>();
                tempList.add(next);
                next = next.getNext();
                while (next != null) {
                    tempList.add(next);
                    if (next.getType() == AbstractInsnNode.METHOD_INSN) {
                        MethodInsnNode method = (MethodInsnNode) next;
                        if (method.getOpcode() == Opcodes.INVOKEVIRTUAL && THRED_CLASS_NAME.equals(method.owner)) {
                            break;
                        }
                    }
                    next = next.getNext();
                }
                removeList.addAll(tempList);
            }
        }
        return removeList;
    }

    private List<String> parseParamFromDesc(String desc) {
        int start = desc.indexOf('(');
        int end = desc.indexOf(')');
        String content = desc.substring(start + 1, end);
        System.out.println(TAG + "parse params from desc " + desc + ",param " + content);

        return parseParam(content);
    }

    public static List<String> parseParam(String desc) {
        List<String> funcArgs = new ArrayList<>();
        boolean waitArrayEnd = false;
        boolean waitObjectEnd = false;
        StringBuilder extend = new StringBuilder();

        for (char c : desc.toCharArray()) {
            switch (c) {
                case 'L':
                    if(!waitObjectEnd){
                        waitObjectEnd = true;
                    }
                    extend.append(c);
                    continue;
                case '[':
                    if(!waitArrayEnd) {
                        waitArrayEnd = true;
                    }
                    extend.append(c);
                    continue;
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 'V':
                    if (waitObjectEnd) {
                        extend.append(c);
                    } else if (waitArrayEnd) {
                        extend.append(c);
                        funcArgs.add(extend.toString());
                        extend.setLength(0);
                        waitArrayEnd = false;
                    } else {
                        funcArgs.add(String.valueOf(c));
                    }
                    continue;
                case ';':
                    if (waitObjectEnd) {
                        extend.append(c);
                        funcArgs.add(extend.toString());
                        extend.setLength(0);
                        waitObjectEnd = false;
                    } else {
                        System.out.println("parseParam error char ';' but extending=false");
                    }
                    continue;
                default:
                    if (waitObjectEnd) {
                        extend.append(c);
                    } else {
                        System.out.println("parseParam unknown char " + c);
                        break;
                    }
            }
        }
        System.out.println("parseParam " + funcArgs.toString());
        return funcArgs;
    }


    private void printInsnList(MethodNode methodNode) {
        System.out.println(TAG + "find target method " + methodNode.name + ", desc " + methodNode.desc);
        if (methodNode.parameters != null) {
            for (ParameterNode param : methodNode.parameters) {
                System.out.println("   param: name=" + param.name);
            }
        } else {
            System.out.println("   no params");
        }
        if (methodNode.localVariables != null) {
            for (LocalVariableNode localVar : methodNode.localVariables) {
                System.out.println("   localVar: name=" + localVar.name + ",index " + localVar.index + ", desc " + localVar.desc);
            }
        } else {
            System.out.println("   no localVar");
        }
        for (AbstractInsnNode insnNode : methodNode.instructions) {
            System.out.println("   insn:  type=" + insnNode.getType() + ",opCode=" + insnNode.getOpcode() + ", toStr=" + insnNode);
//            if (insnNode.getPrevious() != null) {
//                AbstractInsnNode pre = insnNode.getPrevious();
//                System.out.println("       pre:  type=" + pre.getType() + ",opCode=" + pre.getOpcode() + ", toStr=" + pre);
//            }
//            if (insnNode.getNext() != null) {
//                AbstractInsnNode next = insnNode.getNext();
//                System.out.println("       next:  type=" + next.getType() + ",opCode=" + next.getOpcode() + ", toStr=" + next);
//            }
        }
    }
}
