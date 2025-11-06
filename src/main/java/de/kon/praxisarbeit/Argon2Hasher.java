package de.kon.praxisarbeit;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Hasher implements Hasher {
    private static final int ITERATIONS = 2;
    private static final int MEMORY = 65536; // 64 MB
    private static final int PARALLELISM = 1;

    public HashResult hash(String password, int iterations, int memory, int parallelism){
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(iterations, memory, parallelism, password.toCharArray());
        argon2.wipeArray(password.toCharArray());
        return new HashResult("Argon2 Custom", password, hash, "leer, da intern generiert.", 0);
    }

    @Override
    public HashResult hash(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
        argon2.wipeArray(password.toCharArray());
        return new HashResult("Argon2", password, hash, "leer da intern generiert.", 0);
    }

    @Override
    public String getAlgorithmName() {
        return "Argon2";
    }
}

