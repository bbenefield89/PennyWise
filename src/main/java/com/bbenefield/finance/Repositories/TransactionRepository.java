package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TransactionRepository {
    private static Path pathToLedger;
    
    public static boolean getTransactionsFromLedger(String ledgerFileName) {
        Path path = Path.of(LedgerRepository.ledgerDirPath + "/" + ledgerFileName);

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);
            }
            return true;
        }
        catch (IOException e) {
            System.out.println("\nFailed to find ledger: " + e.getMessage() + "\n");
            return false;
        }
    }

//    public static void saveTransactions(List<Transaction> transactions) {
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
//            oos.writeObject(transactions);
//        }
//        catch (IOException e) {
//            System.err.println("Error saving transactions: " + e.getMessage());
//        }
//    }
}
