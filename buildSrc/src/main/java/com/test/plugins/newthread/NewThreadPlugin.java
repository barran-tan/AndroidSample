package com.test.plugins.newthread;

import com.android.build.api.instrumentation.FramesComputationMode;
import com.android.build.api.instrumentation.InstrumentationScope;
import com.android.build.api.variant.AndroidComponentsExtension;
import com.android.build.api.variant.Component;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class NewThreadPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        AndroidComponentsExtension comp = target.getExtensions().getByType(AndroidComponentsExtension.class);
        comp.onVariants(comp.selector().all(), new Action<Component>() {
            @Override
            public void execute(Component variant) {
                variant.transformClassesWith(NewThreadVisitorFactory.class, InstrumentationScope.ALL, v -> null);
                variant.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS);
            }
        });
    }
}
