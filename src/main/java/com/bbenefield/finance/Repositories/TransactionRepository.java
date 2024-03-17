package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private String filePath;

    public TransactionRepository(String filePath) {
        this.filePath = filePath;
    }

    public void saveTransactions(List<Transaction> transactions) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(transactions);
        }
        catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
}
