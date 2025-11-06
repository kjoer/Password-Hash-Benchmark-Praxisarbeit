package de.kon.praxisarbeit;

import com.lambdaworks.crypto.SCryptUtil;
import java.security.SecureRandom;
import java.util.Base64;

public class SCryptHasher implements Hasher {
    private static final int N = 16384;
    private static final int R = 8;
    private static final int P = 1;

    public HashResult hash(String password, int n, int r, int p){
        String hash = SCryptUtil.scrypt(password, n, r, p);
        return new HashResult("SCrypt Custom", password, hash, "leer, da intern generiert", 0);
    }

    @Override
    public HashResult hash(String password) {
        String hash = SCryptUtil.scrypt(password, N, R, P);
        return new HashResult("SCrypt", password, hash, "salt Siehe Parameter im Hash", 0);
    }

    @Override
    public String getAlgorithmName() {
        return "SCrypt";
    }
}