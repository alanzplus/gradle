/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance.generator

import groovy.transform.CompileStatic
import org.gradle.test.fixtures.language.Language

import static org.gradle.test.fixtures.dsl.GradleDsl.KOTLIN

@CompileStatic
enum JavaTestProject {
    LARGE_MONOLITHIC_JAVA_PROJECT(new TestProjectGeneratorConfiguration("largeMonolithicJavaProject")
        .setSourceFiles(50000)
        .setSubProjects(0)
        .setDaemonMemory('1536m')
        .setCompilerMemory('4g')
        .assembleChangeFile(-1)
        .testChangeFile(-1)),
    LARGE_JAVA_MULTI_PROJECT(new TestProjectGeneratorConfiguration("largeJavaMultiProject")
        .setSourceFiles(100)
        .setSubProjects(500)
        .setDaemonMemory('1536m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .testChangeFile(450, 2250, 45000)),
    LARGE_MONOLITHIC_GROOVY_PROJECT(new TestProjectGeneratorConfiguration("largeMonolithicGroovyProject")
        .setSourceFiles(50000)
        .setSubProjects(0)
        .setDaemonMemory('1536m')
        .setCompilerMemory('4g')
        .assembleChangeFile(-1)
        .testChangeFile(-1)
        .setLanguage(Language.GROOVY)),
    LARGE_GROOVY_MULTI_PROJECT(new TestProjectGeneratorConfiguration("largeGroovyMultiProject")
        .setSourceFiles(100)
        .setSubProjects(500)
        .setDaemonMemory('1536m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .testChangeFile(450, 2250, 45000)),
    LARGE_JAVA_MULTI_PROJECT_KOTLIN_DSL(new TestProjectGeneratorConfiguration("largeJavaMultiProjectKotlinDsl", "largeJavaMultiProject")
        .setSourceFiles(100)
        .setSubProjects(500)
        .setDaemonMemory('1536m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .testChangeFile(450, 2250, 45000)
        .setDsl(KOTLIN)
    ),

    MEDIUM_MONOLITHIC_JAVA_PROJECT(new TestProjectGeneratorConfiguration("mediumMonolithicJavaProject")
        .setSourceFiles(10000)
        .setSubProjects(0)
        .setDaemonMemory('512m')
        .setCompilerMemory('1g')
        .assembleChangeFile(-1)
    ),
    MEDIUM_JAVA_MULTI_PROJECT(new TestProjectGeneratorConfiguration("mediumJavaMultiProject")
        .setSourceFiles(100)
        .setSubProjects(100)
        .setDaemonMemory('512m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
    ),
    MEDIUM_JAVA_COMPOSITE_BUILD(new TestProjectGeneratorConfiguration("mediumJavaCompositeBuild", "mediumJavaMultiProject")
        .setSourceFiles(100)
        .setSubProjects(100)
        .setDaemonMemory('768m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .composite(false)
    ),
    MEDIUM_JAVA_PREDEFINED_COMPOSITE_BUILD(new TestProjectGeneratorConfiguration("mediumJavaPredefinedCompositeBuild", "mediumJavaMultiProject")
        .setSourceFiles(100)
        .setSubProjects(100)
        .setDaemonMemory('768m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .composite(true)
    ),
    MEDIUM_JAVA_MULTI_PROJECT_WITH_TEST_NG(new TestProjectGeneratorConfiguration("mediumJavaMultiProjectWithTestNG")
        .setSourceFiles(100)
        .setSubProjects(100)
        .setDaemonMemory('512m')
        .setCompilerMemory('256m')
        .assembleChangeFile()
        .testChangeFile(50, 250, 5000)
        .setUseTestNG(true)
    ),

    SMALL_JAVA_MULTI_PROJECT(new TestProjectGeneratorConfiguration("smallJavaMultiProject")
        .setSourceFiles(50)
        .setSubProjects(10)
        .setDaemonMemory("256m")
        .setCompilerMemory("64m")
        .assembleChangeFile()
    )

    private TestProjectGeneratorConfiguration config

    JavaTestProject(TestProjectGeneratorConfiguration config) {
        this.config = config.build()
    }

    TestProjectGeneratorConfiguration getConfig() {
        return config
    }

    String getProjectName() {
        return config.projectName
    }

    String getDaemonMemory() {
        return config.daemonMemory
    }

    def getParallel() {
        return config.parallel
    }

    def getMaxWorkers() {
        return config.maxWorkers
    }

    @Override
    String toString() {
        return config.projectName
    }
}
