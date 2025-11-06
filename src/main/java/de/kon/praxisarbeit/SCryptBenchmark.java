package de.kon.praxisarbeit;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class SCryptBenchmark {

    @Param({"TestPassword123!"})
    private String password;

    @Param({"16384"})
    private int n;

    @Param({"8"})
    private int r;

    @Param({"1"})
    private int p;

    private SCryptHasher hasher;

    @Setup
    public void setup() {
        hasher = new SCryptHasher();
    }

    @Benchmark
    public HashResult benchmarkSCrypt() {
        return hasher.hash(password, n, r, p);
    }
}
