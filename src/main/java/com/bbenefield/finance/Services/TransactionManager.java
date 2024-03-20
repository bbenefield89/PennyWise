package com.bbenefield.finance.Services;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Repositories.ITransactionRepository;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();
    private final ITransactionRepository transactionRepository;

    public TransactionManager(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(Transaction transaction)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        transactionRepository.createTransaction(transaction);
    }

    public List<Transaction> getTransactions() throws IOException {
        return transactionRepository.getTransactions();
    }

    public List<Transaction> getTransactionsByAmount(double min, double max) throws IOException {
        return transactionRepository.getTransactionsByAmount(min, max);
    }

    public List<Transaction> getTransactionsByDate(LocalDate earliestDate, LocalDate latestDate) throws IOException {
        return transactionRepository.getTransactionsByDate(earliestDate, latestDate);
    }

    public List<Transaction> getTransactionsByType(String type) throws IOException {
        return transactionRepository.getTransactionsByType(type);
    }

    public List<Transaction> getTransactionsByCategory(String category) throws IOException {
        return transactionRepository.getTransactionsByCategory(category);
    }

    public boolean deleteTransactionById(UUID id)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return transactionRepository.deleteTransactionById(id);
    }
}
