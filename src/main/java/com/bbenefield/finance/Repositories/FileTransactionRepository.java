package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileTransactionRepository implements ITransactionRepository {
    private final String pathToLedger;

    public FileTransactionRepository(String pathToLedger) {
        this.pathToLedger = pathToLedger;
    }

    @Override
    public List<Transaction> getTransactions() throws IOException {
        try (FileReader fileReader = new FileReader(pathToLedger)) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(fileReader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }

    public void createTransaction(Transaction transaction) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (FileReader fileReader = new FileReader(pathToLedger)) {
            List<Transaction> transactions = new CsvToBeanBuilder<Transaction>(fileReader)
                    .withType(Transaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
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
}
