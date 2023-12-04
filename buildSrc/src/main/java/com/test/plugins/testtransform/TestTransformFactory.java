package com.test.plugins.testtransform;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;
import com.android.build.api.instrumentation.InstrumentationParameters;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;

public abstract class TestTransformFactory implements AsmClassVisitorFactory<InstrumentationParameters.None> {
    @NotNull
    @Override
    public ClassVisitor createClassVisitor(@NotNull ClassContext classContext, @NotNull ClassVisitor classVisitor) {
        return new TestClassVisitor(classVisitor);
    }

    @Override
    public boolean isInstrumentable(@NotNull ClassData classData) {
        return true;
    }
}
