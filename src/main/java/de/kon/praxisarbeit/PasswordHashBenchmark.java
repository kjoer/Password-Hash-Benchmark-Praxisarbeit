package de.kon.praxisarbeit;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.security.MessageDigest;
import java.util.concurrent.*;


import java.util.*;

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

        // Schnelle Tests mit Standard-Einstellungen (ohne JMH für Übersicht)
        System.out.println("=== Schnelltest mit Standard-Einstellungen (ohne JMH) ===\n");
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit Standard-Einstellungen");
            for (Hasher hasher : hashers) {
                System.out.print("  " + hasher.getAlgorithmName() + "... ");
                long start = System.currentTimeMillis();
                HashResult result = hasher.hash(password);
                long end = System.currentTimeMillis();
                result.setExecutionTimeMs(end - start);
                allResults.add(result);
                System.out.println(result);
            }
            System.out.println();
        }

        // JMH Benchmarks mit verschiedenen Einstellungen
        System.out.println("\n=== Präzise JMH-Benchmarks (dauert länger) ===\n");
        BenchmarkSettings benchmarkSettings = new BenchmarkSettings();

        // BCrypt Tests
        System.out.println("BCrypt Benchmarks:");
        int bcryptCount = 0;
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit empfohlenen BCrypt-Einstellungen");
            BCryptHasher bcryptHasher = new BCryptHasher();
            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 6));
            System.out.println("  [" + (++bcryptCount) + "/35] BCrypt cost=6 abgeschlossen");

            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 8));
            System.out.println("  [" + (++bcryptCount) + "/35] BCrypt cost=8 abgeschlossen");

            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 10));
            System.out.println("  [" + (++bcryptCount) + "/35] BCrypt cost=10 abgeschlossen");

            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 12));
            System.out.println("  [" + (++bcryptCount) + "/35] BCrypt cost=12 abgeschlossen");

            allResults.add(benchmarkSettings.hashBcrypt(bcryptHasher, password, 14));
            System.out.println("  [" + (++bcryptCount) + "/35] BCrypt cost=14 abgeschlossen");
        }

        // SCrypt Tests
        System.out.println("\nSCrypt Benchmarks:");
        int scryptCount = 0;
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit empfohlenen SCrypt-Einstellungen");
            SCryptHasher scryptHasher = new SCryptHasher();

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 16384, 8, 1));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=16384,r=8,p=1 abgeschlossen");

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 32768, 8, 1));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=32768,r=8,p=1 abgeschlossen");

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 8, 1));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=65536,r=8,p=1 abgeschlossen");

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 8, 4));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=65536,r=8,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 4, 2));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=65536,r=4,p=2 abgeschlossen");

            allResults.add(benchmarkSettings.hashScrypt(scryptHasher, password, 65536, 16, 1));
            System.out.println("  [" + (++scryptCount) + "/42] SCrypt N=65536,r=16,p=1 abgeschlossen");
        }

        // Argon2 Tests
        System.out.println("\nArgon2 Benchmarks:");
        int argon2Count = 0;
        for (String password : passwords) {
            System.out.println("Teste Passwort: " + password + " mit empfohlenen Argon2-Einstellungen");
            Argon2Hasher argon2Hasher = new Argon2Hasher();

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 2));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=1,m=62500,p=2 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 2));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=62500,p=2 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 3, 62500, 2));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=3,m=62500,p=2 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 3));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=1,m=62500,p=3 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 1, 62500, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=1,m=62500,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=62500,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 3, 62500, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=3,m=62500,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 3));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=62500,p=3 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=62500,p=4 (Duplikat 1) abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 62500, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=62500,p=4 (Duplikat 2) abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 125000, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=125000,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 250000, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=250000,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 500000, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=500000,p=4 abgeschlossen");

            allResults.add(benchmarkSettings.hashArgon2(argon2Hasher, password, 2, 1000000, 4));
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=2,m=1000000,p=4 abgeschlossen");

            // Ohne JMH (zu schnell/zu langsam für sinnvolle JMH-Messung)
            HashResult r1 = argon2Hasher.hash(password, 1, 1000, 2);
            r1.setExecutionTimeMs(1); // Placeholder
            allResults.add(r1);
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=1,m=1000,p=2 (ohne JMH) abgeschlossen");

            HashResult r2 = argon2Hasher.hash(password, 3, 1000000, 2);
            r2.setExecutionTimeMs(1); // Placeholder
            allResults.add(r2);
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=3,m=1000000,p=2 (ohne JMH) abgeschlossen");

            HashResult r3 = argon2Hasher.hash(password, 32, 1000000, 4);
            r3.setExecutionTimeMs(1); // Placeholder
            allResults.add(r3);
            System.out.println("  [" + (++argon2Count) + "/119] Argon2 i=32,m=1000000,p=4 (ohne JMH) abgeschlossen");
        }

        // Excel Export
        try {
            ExcelExporter exporter = new ExcelExporter();
            exporter.exportToExcel(allResults, sysInfo, "password_benchmark.xlsx");
            System.out.println("\n=== Benchmark abgeschlossen! ===");
            System.out.println("Ergebnisse gespeichert in: password_benchmark.xlsx");
            System.out.println("Anzahl Tests: " + allResults.size());
        } catch (Exception e) {
            System.err.println("Fehler beim Excel-Export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
