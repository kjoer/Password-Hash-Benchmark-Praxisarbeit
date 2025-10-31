package de.kon.praxisarbeit;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptHasher implements Hasher {
    private static final int COST_FACTOR = 12;

    public HashResult hash (String password, int costFactor) {
        long startTime = System.currentTimeMillis();
        String hash = BCrypt.withDefaults().hashToString(costFactor, password.toCharArray());
        long endTime = System.currentTimeMillis();
        return new HashResult("BCrypt Custom", password, hash, "N/A (internal)",
                endTime - startTime);
    }

    @Override
    public HashResult hash(String password) {
        long startTime = System.currentTimeMillis();
        String hash = BCrypt.withDefaults().hashToString(COST_FACTOR, password.toCharArray());
        long endTime = System.currentTimeMillis();
        return new HashResult("BCrypt", password, hash, "N/A (internal)",
                endTime - startTime);
    }

    @Override
    public String getAlgorithmName() {
        return "BCrypt";
    }
}