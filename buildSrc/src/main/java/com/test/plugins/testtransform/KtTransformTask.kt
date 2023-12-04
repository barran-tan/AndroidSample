package com.test.plugins.testtransform

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType

import org.gradle.api.file.RegularFile

import org.gradle.api.file.RegularFileProperty

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputDirectory

import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory

import org.gradle.api.tasks.OutputFile

import org.gradle.api.tasks.PathSensitive

import org.gradle.api.tasks.PathSensitivity

import org.gradle.api.tasks.TaskAction
import org.gradle.work.ChangeType

import org.gradle.work.Incremental

import org.gradle.work.InputChanges
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream


abstract class KtTransformTask : DefaultTask() {

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val jars: ListProperty<RegularFile>

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val dirs: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @get:Optional
    @get:Incremental
    @get:InputDirectory
    abstract val inputDir: DirectoryProperty

    @get:Optional
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    open fun execute(changes: InputChanges?) {

        val tempList = mutableListOf<String>()

        JarOutputStream(FileOutputStream(output.get().asFile)).use { jarOutput ->
            // 1，遍历

            processTargetJars(jarOutput, tempList)

            processDirs(jarOutput, tempList)

            // 字节码插桩
            println("KtTransformTask execute asm inject")
//            val visitor = TestClassVisitor()
        }

        println("KtTransformTask execute done size=${tempList.size}")
    }

    private fun processDirs(jarOutput: JarOutputStream, list: MutableList<String>) {
        dirs.get().forEach { directory ->
            val uri = directory.asFile.toURI()
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
//                    println("processDir file: ${file.name}")

                    val relativePath = uri.relativize(file.toURI()).path
                    println("processDir file relativePath: $relativePath")
                    if (relativePath.contains("test")) {
                        list.add(relativePath ?: "")
                    }

                    val replace = relativePath.replace(File.separatorChar, '/')

//                    println(
//                        "processDir file relativePath replace : $replace"
//                    )

                    jarOutput.putNextEntry(JarEntry(replace))
                    file.inputStream().use { inputStream ->
                        inputStream.copyTo(jarOutput)
                    }
                    jarOutput.closeEntry()
                }
            }
        }
    }


    /**
     * 1. 使用jarOutput.putNextEntry创建一个和输入jarEntry同名的新入口到输出jar中。
     * 2. 使用jarFile.getInputStream获取输入jarEntry对应的输入流。
     * 3. 使用InputStream的copyTo方法将输入流复制到JarOutputStream中,完成内容的复制。
     * 4. 用use方法保证流被关闭。
     * 这样,就实现了从输入的JarFile选择性地复制jarEntry到输出的jar文件中。
     */
    private fun processTargetJars(
        jarOutput: JarOutputStream,
        list: MutableList<String>,
    ) {
        jars.get().forEach { file ->
            val asFile = file.asFile
            JarFile(asFile).use { jarFile ->
                println("processJar : $asFile name=${jarFile.name} size=${jarFile.size()}")
                jarFile.entries().iterator().forEach { jarEntry ->
                    runCatching {
//                        println("processJar : ${jarEntry.name}")
                        if (jarEntry.name.contains("test")) {
                            list.add(jarEntry.name)
                        }
                        jarOutput.putNextEntry(JarEntry(jarEntry.name))

                        jarFile.getInputStream(jarEntry).use {
                            it.copyTo(jarOutput)
                        }
                    }.onFailure { e ->
                        println("Copy jar entry failed. [entry:${jarEntry.name}]")
                    }
                    jarOutput.closeEntry()
                }
            }
        }
    }

    private fun handleChanges(changes: InputChanges?) {
        val inputChanges = if (changes == null) {
            println("execute changes null")
            return
        } else {
            changes
        }
        inputChanges.getFileChanges(inputDir).forEach { change ->
            if (change.fileType == FileType.FILE) {

                val targetFile = outputDir.file(change.normalizedPath).get().asFile
                when (change.changeType) {
                    ChangeType.REMOVED -> {
                        println("kt execute REMOVED $targetFile")
                    }

                    ChangeType.ADDED -> {
                        println("kt execute ADDED $targetFile")
                    }

                    else -> {
                        println("kt execute other $targetFile")
                    }
                }
            }
        }
    }
}