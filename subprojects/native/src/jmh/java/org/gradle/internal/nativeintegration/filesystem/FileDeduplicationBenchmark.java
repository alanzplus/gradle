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
package org.gradle.internal.nativeintegration.filesystem;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Benchmark                                    Mode  Cnt   Score   Error  Units
 * FileDeduplicationBenchmark.absolutePathSet  thrpt    5  36.340 ± 1.500  ops/s
 * FileDeduplicationBenchmark.fileSet          thrpt    5  24.221 ± 0.360  ops/s
 **/
@Fork(2)
@State(Scope.Benchmark)
@Warmup(iterations = 0, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
public class FileDeduplicationBenchmark {
    final ArrayList<File> values = new ArrayList<File>();

    @Setup
    public void prepare() {
        Collection<File> files = FileUtils.listFiles(
            new File("/Users/paplorinc/gradle"), // TODO
            new IOFileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".java");
                }

                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".java");
                }
            },
            TrueFileFilter.INSTANCE
        );
        values.addAll(files);
        values.addAll(files); // duplicates
        values.trimToSize();
        System.out.println("values" + values.size());
    }

    @Benchmark
    public Object fileSet() {
        ImmutableSet.Builder<File> builder = ImmutableSet.builder();
        for (File file : values) {
            builder.add(file);
        }
        return builder.build();
    }

    @Benchmark
    public Object absolutePathSet() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (File file : values) {
            builder.add(file.getAbsolutePath());
        }
        return builder.build();
    }
}
