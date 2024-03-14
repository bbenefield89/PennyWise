package com.bbenefield.finance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter transaction amount:");
        double amount = scanner.nextDouble();

        System.out.println("Enter transaction type (Income/Expense):");
        String type = scanner.next();

        System.out.println("Enter transaction category:");
        String category = scanner.next();

        System.out.println("Enter transaction description (optional):");
        scanner.nextLine();
        String description = scanner.nextLine();

        Transaction transaction = new Transaction(
                transactions.size(),
                amount,
                LocalDate.now(),
                type,
                category,
                description
        );

        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public boolean deleteTransactionById(int id) {
        return transactions.removeIf(transaction -> transaction.getId() == id);
    }
}
