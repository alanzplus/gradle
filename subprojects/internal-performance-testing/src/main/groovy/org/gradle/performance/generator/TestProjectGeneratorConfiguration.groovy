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
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import org.gradle.test.fixtures.dsl.GradleDsl
import org.gradle.test.fixtures.language.Language

import static org.gradle.integtests.fixtures.RepoScriptBlockUtil.mavenCentralRepositoryDefinition

@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
class TestProjectGeneratorConfiguration {
    TestProjectGeneratorConfiguration(String projectName) {
        this(projectName, projectName)
    }

    TestProjectGeneratorConfiguration(String projectName, String templateName) {
        this.projectName = projectName
        this.templateName = templateName
    }
    String projectName
    String templateName

    GradleDsl dsl = GradleDsl.GROOVY
    Language language = Language.JAVA

    String[] plugins = ['groovy', 'eclipse', 'idea']
    String[] repositories
    String[] externalApiDependencies = ['commons-lang:commons-lang:2.5', 'commons-httpclient:commons-httpclient:3.0',
                                        'commons-codec:commons-codec:1.2', 'org.slf4j:jcl-over-slf4j:1.7.10']
    String[] externalImplementationDependencies = ['com.googlecode:reflectasm:1.01']

    int subProjects
    int sourceFiles
    int minLinesOfCodePerSourceFile = 100
    CompositeConfiguration compositeBuild

    String daemonMemory
    String compilerMemory
    String testRunnerMemory = '256m'
    boolean parallel
    int maxWorkers = 4
    int maxParallelForks
    int testForkEvery = 1000
    boolean useTestNG
    Map<String, String> fileToChangeByScenario = [:]

    TestProjectGeneratorConfiguration assembleChangeFile(int project = 0, int pkg = 0, int file = 0) {
        fileToChangeByScenario['assemble'] = productionFile(project, pkg, file)
        return this
    }

    TestProjectGeneratorConfiguration testChangeFile(int project = 0, int pkg = 0, int file = 0) {
        fileToChangeByScenario['test'] = productionFile(project, pkg, file)
        return this
    }

    TestProjectGeneratorConfiguration composite(boolean predifined) {
        compositeBuild = CompositeConfiguration.composite(predifined)
        return this
    }

    private String productionFile(int project = 0, int pkg = 0, int file = 0) {
        if (project >= 0) {
            "project${project}/src/main/${language.name}/org/gradle/test/performance/${templateName.toLowerCase()}/project${project}/p${pkg}/Production${file}.${language.name}"
        } else {
            "src/main/${language.name}/org/gradle/test/performance/${templateName.toLowerCase()}/p${pkg}/Production${file}.${language.name}"
        }
    }

    TestProjectGeneratorConfiguration build() {
        repositories = [mavenCentralRepositoryDefinition(dsl)]
        parallel = subProjects > 0
        maxParallelForks = subProjects > 0 ? 1 : 4
        return this
    }
}
