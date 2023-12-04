package com.test.plugins.testtransform;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileType;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.ChangeType;
import org.gradle.work.Incremental;
import org.gradle.work.InputChanges;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import kotlin.io.ByteStreamsKt;
import kotlin.io.FileTreeWalk;
import kotlin.io.FileWalkDirection;

public abstract class TransformTask extends DefaultTask {

    @InputFiles
    abstract ListProperty<RegularFile> getAllJars();

    @InputFiles
    abstract ListProperty<Directory> getAllDirectories();

    @OutputFiles
    abstract RegularFileProperty getOutput();

    @Optional
    @Incremental
    @InputDirectory
    abstract DirectoryProperty getInputDir();

    @Optional
    @OutputDirectory
    abstract DirectoryProperty getOutputDir();

    @TaskAction
    public void execute(InputChanges changes) {
        System.out.println("execute " + changes);

        ArrayList<String> tempList = new ArrayList<>();

        try {
            JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(getOutput().get().getAsFile()));

            processDirs(outputStream, tempList);

            processJars(outputStream, tempList);

            // asm inject
            System.out.println("TransformTask execute asm inject");
            outputStream.close();
        } catch (IOException e) {
            System.out.println("execute error occur " + e.getMessage());
        }

        System.out.println("TransformTask execute done size=" + tempList.size());
    }

    private void processDirs(JarOutputStream outputStream, ArrayList<String> list) {
        getAllDirectories().get().forEach(directory -> {
            URI dirUri = directory.getAsFile().toURI();
            Iterator<File> walk = new FileTreeWalk(directory.getAsFile(), FileWalkDirection.TOP_DOWN).iterator();
            File item;
            try {
                while (walk.hasNext()) {
                    item = walk.next();

                    if (item.isFile()) {
                        String path = dirUri.relativize(item.toURI()).getPath();
                        System.out.println("processDir file relativePath: " + path);
                        if (path.contains("test")) {
                            list.add(path);
                        }
                        String replace = path.replace(File.separatorChar, '/');

                        outputStream.putNextEntry(new JarEntry(replace));
                        FileInputStream inputStream = new FileInputStream(item);
                        ByteStreamsKt.copyTo(inputStream, outputStream, 8 * 1024);
                        inputStream.close();
                        outputStream.closeEntry();
                    }
                }
            } catch (IOException e) {
                System.out.println("processDirs error occur " + e.getMessage());
            }
        });
    }

    private void processJars(JarOutputStream outputStream, ArrayList<String> list) {
        getAllJars().get().forEach(file -> {
            File _file = file.getAsFile();
            JarFile jarFile;
            try {
                jarFile = new JarFile(_file);
            } catch (IOException e) {
                System.out.println("processJar 1 error occur " + e.getMessage());
                return;
            }
            System.out.printf(Locale.CHINA, "processJars: name=%s size=%d%n", jarFile.getName(), jarFile.size());
            Enumeration<JarEntry> entries = jarFile.entries();
            JarEntry entry;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.getName().contains("test")) {
                    list.add(entry.getName());
                }
                try {
                    outputStream.putNextEntry(new JarEntry(entry.getName()));
                    InputStream inputStream = jarFile.getInputStream(entry);
                    ByteStreamsKt.copyTo(inputStream, outputStream, 8 * 1024);
                    inputStream.close();
                    outputStream.closeEntry();
                } catch (IOException e) {
                    System.out.println("processJar error occur " + e.getMessage());
                    try {
                        outputStream.closeEntry();
                    } catch (IOException ex) {
                        System.out.println("processJars closeEntry error occur " + e.getMessage());
                    }
                }
            }
            try {
                jarFile.close();
            } catch (IOException e) {
                System.out.println("processJar 2 error occur " + e.getMessage());
            }
        });
    }

    private void handleChanges(InputChanges changes) {
        System.out.println("TransformTask handleChanges " + changes);
        if (changes != null) {
            changes.getFileChanges(getInputDir()).forEach(change -> {
                if (change.getFileType() == FileType.FILE) {
                    File targetFile = (File) getOutputDir().file(change.getNormalizedPath()).get();
                    ChangeType changeType = change.getChangeType();
                    if (changeType == ChangeType.REMOVED) {
                        targetFile.delete();
                        System.out.println("file removed " + targetFile);
                    } else if (changeType == ChangeType.ADDED) {
                        System.out.println("file added " + targetFile);
                    }
                }
            });
        } else {

        }
    }
}
