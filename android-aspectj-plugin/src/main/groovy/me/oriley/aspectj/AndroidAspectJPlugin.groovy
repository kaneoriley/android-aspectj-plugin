/*
 * Copyright (C) 2016 Kane O'Riley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.oriley.aspectj

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.tasks.compile.JavaCompile

class AndroidAspectJPlugin implements Plugin<Project> {

    static final String DEPENDENCY = 'org.aspectj:aspectjrt:1.8.8'

    @Override
    void apply(Project project) {
        def variants = null;
        def plugin = project.plugins.findPlugin("android");
        if (plugin != null) {
            variants = "applicationVariants";
        } else {
            plugin = project.plugins.findPlugin("android-library");
            if (plugin != null) {
                variants = "libraryVariants";
            }
        }

        if (variants == null) {
            throw new ProjectConfigurationException("android or android-library plugin must be applied", null)
        }

        addDependency(project, DEPENDENCY)

        project.afterEvaluate {
            project.android[variants].all { variant ->
                JavaCompile javaCompile = variant.javaCompile
                javaCompile.doLast {

                    def bootClasspath
                    //noinspection GroovyAssignabilityCheck
                    if (plugin.properties['runtimeJarList']) {
                        bootClasspath = plugin.runtimeJarList
                    } else {
                        bootClasspath = project.android.bootClasspath
                    }

                    def String[] args = [
                            "-Xlint:cantFindTypeAffectingJPMatch=warning",
                            "-Xlint:cantFindType=warning",
                            "-showWeaveInfo",
                            "-preserveAllLocals",
                            "-encoding", "UTF-8",
                            "-${project.android.compileOptions.sourceCompatibility}",
                            "-inpath", javaCompile.destinationDir.absolutePath,
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", javaCompile.destinationDir.absolutePath,
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", bootClasspath.join(File.pathSeparator),
                    ]

                    def log = project.logger
                    MessageHandler handler = new MessageHandler(true);
                    new Main().run(args, handler);

                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                throw new GradleException(message.message, message.thrown)
                            case IMessage.WARNING:
                                log.warn message.message, message.thrown
                                break;
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break;
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break;
                        }
                    }
                }
            }
        }
    }

    void addDependency(Project project, String dependency) {
        project.dependencies {
            compile dependency
        }
    }
}
