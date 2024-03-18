package com.bbenefield.finance.Services;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Repositories.ITransactionRepository;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();
    private final ITransactionRepository transactionRepository;

    public TransactionManager(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        try {
            return transactionRepository.getTransactions();
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find ledger: " + e.getMessage() + "\n");
            return Collections.emptyList();
        }
    }

    public List<Transaction> getTransactionsByAmount(double min, double max) {
        return this.transactions.stream().filter(transaction -> {
            double transactionAmount = transaction.getAmount();
            return transactionAmount >= min && transactionAmount <= max;
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDate(LocalDate earliestDate, LocalDate latestDate) {
        return this.transactions.stream().filter(transaction -> {
            LocalDate transactionDate = transaction.getDate();
            return (transactionDate.isAfter(earliestDate) || transactionDate.isEqual(earliestDate)) &&
                    (transactionDate.isBefore(latestDate) || transactionDate.isEqual(latestDate));
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByType(String type) {
        return this.transactions.stream().filter(transaction -> {
            return transaction.getType().equals(type);
        }).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        return this.transactions.stream().filter(transaction -> {
            return transaction.getCategory().equals(category);
        }).collect(Collectors.toList());
    }

    public boolean deleteTransactionById(int id) {
        return transactions.removeIf(transaction -> transaction.getId() == id);
    }
}
