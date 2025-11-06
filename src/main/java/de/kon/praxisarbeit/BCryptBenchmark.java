package de.kon.praxisarbeit;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class BCryptBenchmark {

    @Param({"TestPassword123!"})
    private String password;

    @Param({"12"})
    private int cost;

    private BCryptHasher hasher;

    @Setup
    public void setup() {
        hasher = new BCryptHasher();
    }

    @Benchmark
    public HashResult benchmarkBCrypt() {
        return hasher.hash(password, cost);
    }
}