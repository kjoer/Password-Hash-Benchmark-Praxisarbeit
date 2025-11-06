package de.kon.praxisarbeit;


public interface Hasher {
    HashResult hash(String password);
    String getAlgorithmName();
}
