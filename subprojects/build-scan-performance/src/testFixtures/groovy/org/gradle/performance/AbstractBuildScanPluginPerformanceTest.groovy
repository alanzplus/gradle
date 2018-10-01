/*
 * Copyright 2018 the original author or authors.
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

package org.gradle.performance

import groovy.json.JsonSlurper
import org.gradle.integtests.fixtures.executer.IntegrationTestBuildContext
import org.gradle.performance.fixture.BuildExperimentRunner
import org.gradle.performance.fixture.BuildExperimentSpec
import org.gradle.performance.fixture.BuildScanPerformanceTestRunner
import org.gradle.performance.fixture.CrossBuildPerformanceTestRunner
import org.gradle.performance.fixture.GradleSessionProvider
import org.gradle.performance.measure.Amount
import org.gradle.performance.measure.MeasuredOperation
import org.gradle.performance.results.BaselineVersion
import org.gradle.performance.results.BuildScanResultsStore
import org.gradle.performance.results.CrossBuildPerformanceResults
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class AbstractBuildScanPluginPerformanceTest extends Specification {

    int warmupBuilds = 2
    int measuredBuilds = 7

    @Rule
    TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    @AutoCleanup
    @Shared
    def resultStore = new BuildScanResultsStore()

    protected final IntegrationTestBuildContext buildContext = new IntegrationTestBuildContext()
    CrossBuildPerformanceTestRunner runner


    void setup() {
        def incomingDir = "../../incoming" // System.getProperty('incomingArtifactDir')
        assert incomingDir: "'incomingArtifactDir' system property is not set"
        def buildStampJsonFile = new File(incomingDir, "buildStamp.json")
        assert buildStampJsonFile.exists()

        def versionJsonData = new JsonSlurper().parse(buildStampJsonFile) as Map<String, ?>
        assert versionJsonData.commitId
        def pluginCommitId = versionJsonData.commitId as String
        runner = new BuildScanPerformanceTestRunner(new BuildExperimentRunner(new GradleSessionProvider(buildContext)), resultStore, pluginCommitId, buildContext) {
            @Override
            protected void defaultSpec(BuildExperimentSpec.Builder builder) {
                super.defaultSpec(builder)
                builder.workingDirectory = tmpDir.testDirectory
            }
        }

    }

    protected static BaselineVersion buildBaselineResults(CrossBuildPerformanceResults results, String name) {
        def baselineResults = new BaselineVersion(name)
        baselineResults.results.name = name
        baselineResults.results.addAll(results.buildResult(name))
        return baselineResults
    }

    protected static BaselineVersion buildShiftedResults(CrossBuildPerformanceResults results, String name, int maxPercentageShift) {
        def baselineResults = new BaselineVersion(name)
        baselineResults.results.name = name
        def rawResults = results.buildResult(name)
        def shift = rawResults.totalTime.median.value * maxPercentageShift / 100
        baselineResults.results.addAll(rawResults.collect {
            new MeasuredOperation([start: it.start, end: it.end, totalTime: Amount.valueOf(it.totalTime.value + shift, it.totalTime.units), exception: it.exception])
        })
        return baselineResults
    }

}