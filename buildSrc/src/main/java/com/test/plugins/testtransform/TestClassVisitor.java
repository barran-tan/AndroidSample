package com.test.plugins.testtransform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class TestClassVisitor extends ClassVisitor {
    public TestClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("visit " + name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
