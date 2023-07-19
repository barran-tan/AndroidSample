package com.test.plugins.newthread;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;
import com.android.build.api.instrumentation.InstrumentationParameters;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;


public abstract class NewThreadVisitorFactory implements AsmClassVisitorFactory<InstrumentationParameters.None> {

    @NotNull
    @Override
    public ClassVisitor createClassVisitor(@NotNull ClassContext classContext, @NotNull ClassVisitor classVisitor) {
        return new NewThreadClassVisitor(classVisitor);
    }

    @Override
    public boolean isInstrumentable(@NotNull ClassData classData) {
        String className = classData.getClassName();
        if (className.contains("asmtest") || className.startsWith("com.appsflyer.")) {
            return true;
        }
        return false;
    }
}
