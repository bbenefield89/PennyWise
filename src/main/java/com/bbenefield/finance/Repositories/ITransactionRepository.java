package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITransactionRepository {
    List<Transaction> getTransactions() throws IOException;

    List<Transaction> getTransactionsByAmount(double min, double max) throws IOException;

    List<Transaction> getTransactionsByDate(LocalDate earliestDate, LocalDate latestDate) throws IOException;

    List<Transaction> getTransactionsByType(String transactionType) throws IOException;

    List<Transaction> getTransactionsByCategory(String transactionCategory) throws IOException;

    void createTransaction(Transaction transaction) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    boolean deleteTransactionById(UUID transactionId) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
