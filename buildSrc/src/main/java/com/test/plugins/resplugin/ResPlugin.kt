package com.test.plugins.resplugin

import com.android.aapt.Resources
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.res.LinkAndroidResForBundleTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.build.gradle.tasks.MergeResources
import com.android.tools.build.bundletool.model.utils.ResourcesUtils
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.services.BuildService
import org.gradle.api.tasks.FileNormalizer
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.fingerprint.DirectorySensitivity
import org.gradle.internal.fingerprint.LineEndingSensitivity
import org.gradle.internal.properties.InputBehavior
import org.gradle.internal.properties.InputFilePropertyType
import org.gradle.internal.properties.OutputFilePropertyType
import org.gradle.internal.properties.PropertyValue
import org.gradle.internal.properties.PropertyVisitor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class ResPlugin : Plugin<Project> {

    private val appNamespacePrefix = "app"
    private val appNamespaceUri = "http://schemas.android.com/apk/res-auto"

    private var imgSrcId = 0

    override fun apply(target: Project) {
        val comp = target.extensions.getByType(
            AndroidComponentsExtension::class.java
        )
        val tasks = target.gradle.startParameter.taskNames
        val taskNames: MutableList<String> = ArrayList(tasks.size)
        for (taskName in tasks) {
            taskNames.add(taskName.toLowerCase())
            println("--------apply task:$taskName")
        }

        comp.onVariants { variant ->
            val variantName = variant.name.toLowerCase()
            var targetVariant = false
            for (taskName in taskNames) {
                if (taskName.contains(variantName)) {
                    targetVariant = true
                    break
                }
                if (taskName.contains("release") && variantName.contains("release")) {
                    targetVariant = true
                    break
                }
                if (taskName.contains("debug") && variantName.contains("debug")) {
                    targetVariant = true
                    break
                }
            }
            if (targetVariant) {
                setup(target)
            }
        }
    }

    private fun setup(project: Project) {
        val android = project.extensions.getByName("android") as AppExtension
        project.afterEvaluate {
            android.applicationVariants.all(object : Action<ApplicationVariant> {
                override fun execute(t: ApplicationVariant) {

//                if (project.getPlugins().hasPlugin("com.android.application")) {
//                    CodeCoverageExtension coverageExtension = (CodeCoverageExtension) project.getExtensions().getByName("codeCoverage");
//                    if (coverageExtension != null) {
//                        System.out.println("codeCoverage config:" + coverageExtension.toString());
//                        CodeCoverageHelper.updateConfig(coverageExtension);
//                    }
//                }

                    addTask(project, t)
                }
            })
        }
    }

    private fun addTask(project: Project, variant: ApplicationVariant) {
        val temp = variant.name
        val variantName = temp.toUpperCase().substring(0, 1) + temp.substring(1)
        val customTask = project.tasks.register(
            temp + CustomTask.TASK_NAME,
            CustomTask::class.java
        )
        try {
            val mergeRes: TaskProvider<Task> = project.tasks.named("merge" + variantName + "Resources")
            customTask.dependsOn(mergeRes)
            println("addTask depends pre $mergeRes")
            mergeRes.configure {
                println("mergeRes task $this")
                if(this is MergeResources) {
                    val mergeResources = this
                    doLast {
                        val directory = mergeResources.outputDir.get()
                        val outputDir = mergeResources.outputs
                        println("outputDir $directory")
                        println("outputs $outputDir")
                    }
                }
            }
        } catch (e: Exception) {
            println("addTask fail  " + e.message)
        }
        try {
            val processRes: TaskProvider<*> =
                project.tasks.named("process" + variantName + "Resources")
            processRes.dependsOn(customTask)
            println("addTask depends after $processRes")
            processRes.configure {
                println("processRes task $this")
                if(this is LinkApplicationAndroidResourcesTask){
                    val linkResources = this
                    doLast {
                        val sourceOutputDir = linkResources.sourceOutputDir
                        println("sourceOutputDir $sourceOutputDir")

                        if (linkResources.rClassOutputJar.isPresent) {
                            println("R output ${linkResources.rClassOutputJar.asFile.get()}")
                        }
                        println("resPackageOutputFolder ${linkResources.resPackageOutputFolder.asFile.get()}")
                        if (linkResources.sourceOutputDirProperty.isPresent) {
                            println("sourceOutputDirProperty ${linkResources.sourceOutputDirProperty.asFile.get()}")
                        }
                        println("outputs ${linkResources.outputs}")
                        linkResources.outputs.visitRegisteredProperties(object :
                            PropertyVisitor{


                            override fun visitInputFileProperty(
                                propertyName: String,
                                optional: Boolean,
                                behavior: InputBehavior,
                                directorySensitivity: DirectorySensitivity,
                                lineEndingSensitivity: LineEndingSensitivity,
                                fileNormalizer: org.gradle.internal.fingerprint.FileNormalizer?,
                                value: PropertyValue,
                                filePropertyType: InputFilePropertyType
                            ) {
                                println("visitInputFileProperty name=$propertyName value=${value.call()} optional=$optional")
                            }

                            override fun visitLocalStateProperty(value: Any) {
                                println("visitLocalStateProperty value=$value")
                            }

                            override fun visitServiceReference(
                                propertyName: String,
                                optional: Boolean,
                                value: PropertyValue,
                                serviceName: String?,
                                buildServiceType: Class<out BuildService<*>>
                            ) {
                                println("visitServiceReference name=$propertyName value=${value.call()} optional=$optional")
                            }

                            override fun visitOutputFileProperty(
                                propertyName: String,
                                optional: Boolean,
                                value: PropertyValue,
                                filePropertyType: OutputFilePropertyType
                            ) {
                                println("visitOutputFileProperty name=$propertyName value=${value.call()} file=${filePropertyType.name}-${filePropertyType.ordinal} optional=$optional")
                            }

                            override fun visitDestroyableProperty(value: Any) {
                                println("visitDestroyableProperty value=$value")
                            }
                        })
                    }
                }
            }

            try {
                val bundleRes: TaskProvider<Task> = project.tasks.named("bundle" + variantName + "Resources")
//                customTask.dependsOn(bundleRes)
//                println("addTask depends after $bundleRes")
                bundleRes.configure {
                    println("bundleResources task $this")
                    if(this is LinkAndroidResForBundleTask) {
                        val bundleResources = this
                        doLast {
                            val outputDir = bundleResources.bundledResFile.asFile.get()
                            println("bundledResFile $outputDir")
                            resAnalyze(outputDir)
                        }
                    }
                }
            } catch (e: Exception) {
                println("addTask fail  " + e.message)
            }
        } catch (e: Exception) {
            println("addTask fail  " + e.message)
        }
    }

    fun resAnalyze(resApk: File) {

        val resZip = ZipFile(resApk)
//            val appBundle = AppBundle.buildFromZip(bundleZip)
//            for ((key, value) in appBundle.modules.entries) {
//            }
        val maps = mutableMapOf<String, Resources.XmlNode>()
        for (entry in resZip.entries()) {
            val node = scanRes(resZip, entry)
            if (node != null) {
                maps[entry.name] = node
            }
        }
        if(maps.isNotEmpty()){
            // update zip entry
            appendNodeToZip(resZip,resApk,maps)
        }
    }

    private fun scanRes(zip: ZipFile, entry: ZipEntry): Resources.XmlNode? {
        val name = entry.name
        if(name.endsWith("resources.pb")){
            val inputStream = zip.getInputStream(entry)
            val resTable = Resources.ResourceTable.parseFrom(inputStream)
            parseResTable(resTable)
            return null
        }
        var element:Resources.XmlNode? = null
        if (name.startsWith("res/layout") && name.endsWith(".xml")) {
//            println("scanRes $name")
            val inputStream = zip.getInputStream(entry)
            val raw: Resources.XmlNode =
                Resources.XmlNode.parseFrom(inputStream)
            if (raw.hasElement()) {
                val rootElement: Resources.XmlElement = raw.element
                if (rootElement !== Resources.XmlElement.getDefaultInstance()) {
                    scanRes(rootElement)

                    if (name.contains("activity_other.xml")) {
                        element = updateRes(raw)
                    }
                }
            }

            inputStream.close()
        }
        return element
    }

    private fun scanRes(element: Resources.XmlElement) {
        if (element.name == "ImageView") {
            val attrCount = element.attributeCount
            for (i in 0 until attrCount) {
                val attr = element.getAttribute(i)
                if (attr.name == "src" || attr.name == "background") {
                    println("attr name=${attr.name} value=${attr.value}")
                }
            }
        } else {
            val childCount = element.childCount
            for (i in 0 until childCount) {
                if (element.getChild(i).hasElement()) {
                    scanRes(element.getChild(i).element)
                }
            }
        }
    }

    private fun updateRes(node: Resources.XmlNode): Resources.XmlNode {
        if (node.hasElement()) {
            val element = node.element
            val builder = element.toBuilder()

            var hasAppNs = false
            for (ns in element.namespaceDeclarationList) {
                if (ns.uri == appNamespaceUri) {
                    hasAppNs = true
                    break
                }
            }
            if (!hasAppNs) {
                builder.addNamespaceDeclaration(
                    Resources.XmlNamespace.newBuilder().setPrefix(appNamespacePrefix).setUri(appNamespaceUri)
                        .build()
                )
            }

            if (element.name == "ImageView") {
                val attrCount = element.attributeCount
                val removeAttrs = mutableListOf<Int>()
                for (i in 0 until attrCount) {
                    val attr = element.getAttribute(i)
                    if (attr.name == "src"/* || attr.name == "background"*/) {
                        println("attr name=${attr.name} value=${attr.value}")
                        // index out of bounds
//                        builder.removeAttribute(i)
//                        removeAttrs.add(i)

                        builder.setAttribute(i,
                            attr.newBuilderForType().setNamespaceUri(appNamespaceUri).setName("imgSrc").setValue(attr.value)
                                .setResourceId(imgSrcId).build()
                        )
                    } else {
                        builder.setAttribute(i, attr)
                    }
                }
//                for (index in removeAttrs) {
//                    builder.removeAttribute(index)
//                }
            } else {
                val childCount = element.childCount
                for (i in 0 until childCount) {
                    if (element.getChild(i).hasElement()) {
                        val updateNode = updateRes(element.getChild(i))
                        builder.setChild(i, updateNode)
                    }
                }
            }
            return node.toBuilder().setElement(builder.build()).build()
        } else {
            return node
        }
    }

    private fun parseResTable(resTable: Resources.ResourceTable) {
        val entry = ResourcesUtils.lookupEntryByResourceTypeAndName(resTable, "attr", "imgSrc")
        if (entry.isPresent) {
            val entry1 = entry.get()
            println("parseResTable entryId=${entry1.entryId.id}")
        } else {
            println("parseResTable lookup Entry failed")
        }
        for (pkg in resTable.packageList) {
            val pkgId = pkg.packageId.id
            for (type in pkg.typeList) {
                if (type.name == "attr") {
                    val typeId = type.typeId.id
                    for (entry in type.entryList) {
                        if (entry.name.contains("imgSrc")) {
                            val entryId = entry.entryId.id
                            println("find imgSrc entryId = $entryId typeId=$typeId pkgId=$pkgId")
                            imgSrcId = getFullResourceId(pkgId, typeId, entryId)
                            println("imgSrc resourceId=$imgSrcId")
                            return
                        }
                    }
                }
            }
        }
    }

    private fun getFullResourceId(pkgId: Int, typeId: Int, entryId: Int): Int {
        return (pkgId shl 24) + (typeId shl 16) + entryId
    }

    private fun appendNodeToZip(zip: ZipFile,zipFile:File,maps:Map<String, Resources.XmlNode>){
        val newFile = File(zipFile.parent,"temp.zip")
        val newZip = ZipOutputStream(FileOutputStream(newFile))
        val keys = maps.keys
        try {
            for (entry in zip.entries()) {

                if (entry.name in keys) {
                    println("appendNodeToZip ignore cause append : ${entry.name}")
                    continue
                }

                val newEntry = ZipEntry(entry.name)
                newZip.putNextEntry(newEntry)

                val inputStream = zip.getInputStream(entry)
                val buffer = ByteArray(4096)
                var read: Int
                while (true) {
                    read = inputStream.read(buffer)
                    if(read != -1){

//                        newZip.write(buffer) // 直接这么使用会导致写入了无效的字符，其他任务解析pb失败
                        newZip.write(buffer, 0, read)
                    }else{
                        break
                    }
                }
                inputStream.close()
                newZip.closeEntry()
            }
            zip.close()
        }catch (e:IOException){
            println("appendNodeToZip error ${e.message}")
        }

        // append node
        for(entries in maps.entries) {
            val appendEntry = ZipEntry(entries.key)
            newZip.putNextEntry(appendEntry)
            newZip.write(entries.value.toByteArray())
            newZip.closeEntry()
        }
        newZip.close()

        // rename
        zipFile.delete()
        newFile.renameTo(zipFile)
    }
}