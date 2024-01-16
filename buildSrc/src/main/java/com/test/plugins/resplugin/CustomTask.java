package com.test.plugins.resplugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class CustomTask extends DefaultTask {
    public static final String TASK_NAME = "Custom";

    @TaskAction
    public void action(){
        System.out.println("----CustomTask start action");
    }
}
