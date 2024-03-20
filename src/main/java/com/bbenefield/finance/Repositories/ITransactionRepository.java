package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ITransactionRepository {
    List<Transaction> getTransactions() throws IOException;

    List<Transaction> getTransactionsByAmount(double min, double max) throws IOException;

    void createTransaction(Transaction transaction) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
