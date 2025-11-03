package de.kon.praxisarbeit;

import com.lambdaworks.crypto.SCryptUtil;
import java.security.SecureRandom;
import java.util.Base64;

public class SCryptHasher implements Hasher {
    private static final int N = 16384; // CPU/Memory cost
    private static final int R = 8;     // Block size
    private static final int P = 1;     // Parallelization

    public HashResult hash(String password, int n, int r, int p){
        long startTime = System.currentTimeMillis();
        String hash= SCryptUtil.scrypt(password, n, r, p);
        long endTime = System.currentTimeMillis();
        return new HashResult("SCrypt Custom",password,hash,"leer, da intern generiert",endTime-startTime);
    }

    @Override
    public HashResult hash(String password) {
        long startTime = System.currentTimeMillis();
        String hash = SCryptUtil.scrypt(password, N, R, P);
        long endTime = System.currentTimeMillis();
        return new HashResult("SCrypt", password, hash, "salt Siehe Parameter im Hash",
                endTime - startTime);
    }

    @Override
    public String getAlgorithmName() {
        return "SCrypt";
    }
}