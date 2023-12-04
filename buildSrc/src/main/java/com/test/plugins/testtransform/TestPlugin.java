package com.test.plugins.testtransform;

import com.android.build.api.artifact.ScopedArtifact;
import com.android.build.api.instrumentation.FramesComputationMode;
import com.android.build.api.instrumentation.InstrumentationScope;
import com.android.build.api.variant.AndroidComponentsExtension;
import com.android.build.api.variant.Component;
import com.android.build.api.variant.ScopedArtifacts;
import com.test.plugins.newthread.NewThreadVisitorFactory;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

public class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        AndroidComponentsExtension comp = target.getExtensions().getByType(AndroidComponentsExtension.class);
        comp.onVariants(comp.selector().all(), new Action<Component>() {
            @Override
            public void execute(Component variant) {

                System.out.println("TestPlugin apply " + variant.getName());
                TaskProvider<TransformTask> transformTask = target.getTasks().register(variant.getName() + "TransformTask", TransformTask.class);

                variant.getArtifacts().forScope(ScopedArtifacts.Scope.ALL).use(transformTask).toTransform(
                        ScopedArtifact.CLASSES.INSTANCE, TransformTask::getAllJars, TransformTask::getAllDirectories, TransformTask::getOutput
                );

//                variant.getInstrumentation().transformClassesWith(TestTransformFactory.class, InstrumentationScope.ALL, v -> null);
//                variant.getInstrumentation().setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS);
            }
        });
    }
}
