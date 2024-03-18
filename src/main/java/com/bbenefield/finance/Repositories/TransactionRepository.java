package com.bbenefield.finance.Repositories;

import com.bbenefield.finance.Models.Transaction;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.util.List;

public class TransactionRepository {
    public static List<Transaction> getTransactionsFromLedger(String pathToLedger) throws FileNotFoundException {
        FileReader fileReader = new FileReader(pathToLedger);
        CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(fileReader)
                .withSkipLines(1)
                .withType(Transaction.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
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
