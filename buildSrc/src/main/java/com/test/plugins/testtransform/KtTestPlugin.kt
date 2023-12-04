package com.test.plugins.testtransform

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationParameters
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.test.plugins.newthread.NewThreadVisitorFactory
import org.gradle.api.Plugin
import org.gradle.api.Project

class KtTestPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val comp = target.extensions.getByType(
            AndroidComponentsExtension::class.java
        )

//        comp.onVariants {
//            println("KtTestPlugin apply " + it.name)

//            it.instrumentation.transformClassesWith(
//                TestTransformFactory::class.java,
//                InstrumentationScope.ALL
//            ) { v: InstrumentationParameters.None? -> null }
//            it.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
//        }

        comp.onVariants { variant ->

            println("KtTestPlugin apply " + variant.name)

            val transformTask = target.tasks.register(
                variant.name + "KtTransformTask",
                KtTransformTask::class.java
            )

            variant.artifacts.forScope(ScopedArtifacts.Scope.ALL).use(transformTask).toTransform(
                ScopedArtifact.CLASSES,
                KtTransformTask::jars,
                KtTransformTask::dirs,
                KtTransformTask::output
            )
        }

    }
}