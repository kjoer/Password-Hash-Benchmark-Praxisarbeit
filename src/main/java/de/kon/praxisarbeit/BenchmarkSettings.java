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

  
}

