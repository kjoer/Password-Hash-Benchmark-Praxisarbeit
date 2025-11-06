package de.kon.praxisarbeit;

public class HashResult {
    private String algorithm;
    private String password;
    private String hash;
    private String salt;
    private long executionTimeMs;
    private long cpuLoad;

    public HashResult(String algorithm, String password, String hash, String salt,
                      long executionTimeMs) {
        this.algorithm = algorithm;
        this.password = password;
        this.hash = hash;
        this.salt = salt;
        this.executionTimeMs = executionTimeMs;
    }

    public String getAlgorithm() { return algorithm; }
    public String getPassword() { return password; }
    public String getHash() { return hash; }
    public String getSalt() { return salt; }
    public long getExecutionTimeMs() { return executionTimeMs; }

    @Deprecated
    public double getCpuLoad() { return cpuLoad; }

    public void setCpuLoad(long cpuLoad){
        this.cpuLoad = cpuLoad;
    }

    public void setExecutionTimeMs(long executionTimeMs){
        this.executionTimeMs = executionTimeMs;
    }

    @Override
    public String toString() {
        return String.format("%s: %dms", algorithm, executionTimeMs);
    }
}