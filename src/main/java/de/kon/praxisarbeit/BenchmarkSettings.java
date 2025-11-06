package de.kon.praxisarbeit;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Simple Utility Klasse für custom Settings
 */

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class BenchmarkSettings {

    public HashResult hashBcrypt(BCryptHasher hasher, String passwordToHash, int cost) {
        try {
            Options opt = new OptionsBuilder()
                    .include(BCryptBenchmark.class.getSimpleName())
                    .warmupIterations(2)
                    .measurementIterations(5)
                    .forks(1)
                    .param("password", passwordToHash)
                    .param("cost", String.valueOf(cost))
                    .build();

            Collection<RunResult> results = new Runner(opt).run();
            double avgTimeMs = results.iterator().next().getPrimaryResult().getScore();

            HashResult sampleResult = hasher.hash(passwordToHash, cost);

            return new HashResult(
                    "BCrypt (cost=" + cost + ")",
                    passwordToHash,
                    sampleResult.getHash(),
                    sampleResult.getSalt(),
                    (long) avgTimeMs
            );
        } catch (Exception e) {
            e.printStackTrace();
            return hasher.hash(passwordToHash, cost);
        }
    }

    public HashResult hashScrypt(SCryptHasher hasher, String passwordToHash, int n, int r, int p) {
        try {
            Options opt = new OptionsBuilder()
                    .include(SCryptBenchmark.class.getSimpleName())
                    .warmupIterations(2)
                    .measurementIterations(5)
                    .forks(1)
                    .param("password", passwordToHash)
                    .param("n", String.valueOf(n))
                    .param("r", String.valueOf(r))
                    .param("p", String.valueOf(p))
                    .build();

            Collection<RunResult> results = new Runner(opt).run();
            double avgTimeMs = results.iterator().next().getPrimaryResult().getScore();

            HashResult sampleResult = hasher.hash(passwordToHash, n, r, p);

            return new HashResult(
                    "SCrypt (N=" + n + ",r=" + r + ",p=" + p + ")",
                    passwordToHash,
                    sampleResult.getHash(),
                    sampleResult.getSalt(),
                    (long) avgTimeMs
            );
        } catch (Exception e) {
            e.printStackTrace();
            return hasher.hash(passwordToHash, n, r, p);
        }
    }

    public HashResult hashArgon2(Argon2Hasher hasher, String passwordToHash, int iterations, int memory, int parallelism) {
        try {
            Options opt = new OptionsBuilder()
                    .include(Argon2Benchmark.class.getSimpleName())
                    .warmupIterations(2)
                    .measurementIterations(5)
                    .forks(1)
                    .param("password", passwordToHash)
                    .param("iterations", String.valueOf(iterations))
                    .param("memory", String.valueOf(memory))
                    .param("parallelism", String.valueOf(parallelism))
                    .build();

            Collection<RunResult> results = new Runner(opt).run();
            double avgTimeMs = results.iterator().next().getPrimaryResult().getScore();

            HashResult sampleResult = hasher.hash(passwordToHash, iterations, memory, parallelism);

            return new HashResult(
                    "Argon2 (i=" + iterations + ",m=" + memory + ",p=" + parallelism + ")",
                    passwordToHash,
                    sampleResult.getHash(),
                    sampleResult.getSalt(),
                    (long) avgTimeMs
            );
        } catch (Exception e) {
            e.printStackTrace();
            return hasher.hash(passwordToHash, iterations, memory, parallelism);
        }
    }


    @Deprecated
    private static HashResult measureHashWithCPULoad(Callable<HashResult> task) {
        MemoryMonitor memoryMonitor = new MemoryMonitor();
        Thread cpuMonitorThread = new Thread(memoryMonitor);
        cpuMonitorThread.start();
        long start = System.nanoTime();
        HashResult result;
        try {
            result = task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        memoryMonitor.stop();
        double durationMs = (end - start) / 1_000_000.0;
        double avgCpuLoad = memoryMonitor.getPeakMB();
        try {
            result.setExecutionTimeMs((long) durationMs);
            result.setCpuLoad((long)avgCpuLoad);
        } catch (Exception ignored) {
        }
        return result;
    }
}
