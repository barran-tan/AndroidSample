package com.test.plugins.dataclass;

import com.android.build.api.instrumentation.FramesComputationMode;
import com.android.build.api.instrumentation.InstrumentationScope;
import com.android.build.api.variant.AndroidComponentsExtension;
import com.android.build.api.variant.Component;
import com.test.plugins.newthread.NewThreadVisitorFactory;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class OptDataClassPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        AndroidComponentsExtension comp = project.getExtensions().getByType(AndroidComponentsExtension.class);
        comp.onVariants(comp.selector().all(), new Action<Component>() {
            @Override
            public void execute(Component variant) {
                variant.getInstrumentation().transformClassesWith(OptDataClassVisitorFactory.class, InstrumentationScope.PROJECT, v -> null);
                variant.getInstrumentation().setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS);
            }
        });
    }
}
