package de.kon.praxisarbeit;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptHasher implements Hasher {
    private static final int COST_FACTOR = 12;

    public HashResult hash(String password, int costFactor) {
        String hash = BCrypt.withDefaults().hashToString(costFactor, password.toCharArray());
        return new HashResult("BCrypt Custom", password, hash, "N/A", 0);
    }

    @Override
    public HashResult hash(String password) {
        return hash(password, COST_FACTOR);
    }

    @Override
    public String getAlgorithmName() {
        return "BCrypt";
    }
}
