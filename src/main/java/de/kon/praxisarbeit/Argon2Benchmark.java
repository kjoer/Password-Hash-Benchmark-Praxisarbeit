package de.kon.praxisarbeit;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class Argon2Benchmark {

    @Param({"TestPassword123!"})
    private String password;

    @Param({"2"})
    private int iterations;

    @Param({"65536"})
    private int memory;

    @Param({"2"})
    private int parallelism;

    private Argon2Hasher hasher;

    @Setup
    public void setup() {
        hasher = new Argon2Hasher();
    }

    @Benchmark
    public HashResult benchmarkArgon2() {
        return hasher.hash(password, iterations, memory, parallelism);
    }
}

