package com.test.plugins.dataclass;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;
import com.android.build.api.instrumentation.InstrumentationParameters;

import org.objectweb.asm.ClassVisitor;


public abstract class OptDataClassVisitorFactory implements AsmClassVisitorFactory<InstrumentationParameters.None> {

    @Override
    public ClassVisitor createClassVisitor(ClassContext classContext, ClassVisitor classVisitor) {
        return new OptDataClassClassVisitor(classVisitor);
    }

    @Override
    public boolean isInstrumentable(ClassData classData) {
        return true;
    }
}
