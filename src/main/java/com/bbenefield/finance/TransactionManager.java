package com.bbenefield.finance;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public boolean deleteTransactionById(int id) {
        return transactions.removeIf(transaction -> transaction.getId() == id);
    }
}
