package de.kon.praxisarbeit;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.security.MessageDigest;
import java.util.concurrent.*;


public class PasswordHashBenchmark {
    public static void main(String[] args) {
        System.out.println("=== Password Hash Benchmark ===\n");

        SysInfo sysInfo = SystemInfoReader.readSystemInfo();
        System.out.println("Systeminformationen:");
        System.out.println(sysInfo);
        System.out.println();
        List<String> passwords = Arrays.asList(
                "123456",
                "bisslKomplexer!12hm?",
                "sehrlangesun2Kd1okmgPa22vowrt!?*da",
                "r73iroszgdf674",
                "nochEineEingabe",
                "rno!gfldih4iuohdl//\"",
                "ghu7823gd87g387g78782"
        );
        List<Hasher> hashers = Arrays.asList(
                new BCryptHasher(),
                new SCryptHasher(),
                new Argon2Hasher()
        );
        List<HashResult> allResults = new ArrayList<>();
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit Standard-Einstellungen");
            for (Hasher hasher : hashers) {
                System.out.print("  " + hasher.getAlgorithmName() + "... ");
                HashResult result = hasher.hash(password);
                allResults.add(result);
                System.out.println(result);
            }
            System.out.println();
        }
        BenchmarkSettings benchmarkSettings = new BenchmarkSettings();
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit Empfohlenen-Einstellungen aus den Quellen");
            BCryptHasher bcryptHasher = new BCryptHasher();
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 6));
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 8));
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 10));
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 12));
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 14));
        }
        for (String password : passwords) {
            SCryptHasher scryptHasher = new SCryptHasher();
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 16384, 8, 1));
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 32768, 8, 1));
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 8, 1));
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 8, 4));
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 4, 2));
            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 16, 1));
        }
        for (String password : passwords) {
            Argon2Hasher argon2Hasher = new Argon2Hasher();
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 2));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 2));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 3, 62500, 2));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 3));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 3, 62500, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 3));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 125000, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 250000, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 500000, 4));
            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 1000000, 4));
            allResults.add(argon2Hasher.hash(password, 1, 1000, 2));
            allResults.add(argon2Hasher.hash(password, 3, 1000000, 2));
            allResults.add(argon2Hasher.hash(password, 32, 1000000, 4));
        }
        try {
            ExcelExporter exporter = new ExcelExporter();
            exporter.exportToExcel(allResults, sysInfo, "password_benchmark.xlsx");
            System.out.println("\nBenchmark abgeschlossen!");
        } catch (
                Exception e) {
            System.err.println("Fehler beim Excel-Export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
