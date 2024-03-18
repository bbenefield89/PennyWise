package com.bbenefield.finance.Repositories;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LedgerRepository {
    public static String ledgerDirPath = "./ledgers";

    public static void createLedgerDirectory() {
        Path path = Path.of(ledgerDirPath);

        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                System.out.println("Directory created successfully");
            }
        }
        catch (IOException e) {
            System.out.println("Failed to create directory: " + e.getMessage());
        }
    }

    public static boolean checkLedgerExistence(String ledgerFileName) {
        Path path = Path.of(String.format("%s/%s", ledgerDirPath, ledgerFileName));
        return Files.exists(path);
    }
}
