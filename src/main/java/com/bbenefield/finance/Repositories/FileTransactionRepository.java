package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileTransactionRepository implements ITransactionRepository {
    private final String pathToLedger;

    public FileTransactionRepository(String pathToLedger) {
        this.pathToLedger = pathToLedger;
    }

    @Override
    public List<Transaction> getTransactions() throws IOException {
        try (FileReader fileReader = new FileReader(pathToLedger)) {
            return createTransactionCsvToBeanBuilder(fileReader)
                    .build()
                    .parse();
        }
    }

    @Override
    public List<Transaction> getTransactionsByAmount(double min, double max) throws IOException {
        try (FileReader fileReader = new FileReader(pathToLedger)) {
            return createTransactionCsvToBeanBuilder(fileReader)
                    .withFilter(line -> {
                        System.out.println(Arrays.toString(line));
                        double amount = Double.parseDouble(line[4]);
                        return amount >= min && amount <= max;
                    })
                    .build()
                    .parse();
        }
    }

    @Override
    public void createTransaction(Transaction transaction) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (FileReader fileReader = new FileReader(pathToLedger)) {
            List<Transaction> transactions = createTransactionCsvToBeanBuilder(fileReader)
                    .build()
                    .parse();

            transactions.add(transaction);

            try (Writer writer = new FileWriter(pathToLedger)) {
                StatefulBeanToCsv<Transaction> beanToCsv = new StatefulBeanToCsvBuilder<Transaction>(writer)
                        .build();
                beanToCsv.write(transactions);
            }
        }
    }

    private CsvToBeanBuilder<Transaction> createTransactionCsvToBeanBuilder(FileReader fileReader) {
        return new CsvToBeanBuilder<Transaction>(fileReader)
                .withType(Transaction.class)
                .withIgnoreLeadingWhiteSpace(true);
    }
}
