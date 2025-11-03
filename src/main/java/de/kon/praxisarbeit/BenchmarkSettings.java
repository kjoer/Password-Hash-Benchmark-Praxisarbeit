package de.kon.praxisarbeit;

import java.util.concurrent.Callable;

/**
 * Simple Utility Klasse für custom Settings
 */
public class BenchmarkSettings {
    public HashResult hashBcrypt(BCryptHasher hasher, String passwordToHash, int cost) {
        System.out.println("Hashing " + passwordToHash + " mit " + hasher.getAlgorithmName());
        return  hasher.hash(passwordToHash, cost);
        //measureHashWithCPULoad(() ->
    }

    public HashResult hashScrypt(SCryptHasher hasher, String passwordToHash, int n, int r, int p) {
        System.out.println("Hashing " + passwordToHash + " mit " + hasher.getAlgorithmName());
        return hasher.hash(passwordToHash, n, r, p);
        //measureHashWithCPULoad(() ->
    }

    public HashResult hashArgon2(Argon2Hasher hasher, String passwordToHash, int iterations, int memory, int parallelism) {
        System.out.println("Hashing " + passwordToHash + " mit " + hasher.getAlgorithmName());
        return hasher.hash(passwordToHash, iterations, memory, parallelism);
        //measureHashWithCPULoad(() ->
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
