package com.bbenefield.finance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TransactionManager transactionManager = new TransactionManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            String statement = "Choose an option:\n";
            statement += "[1] Add Transaction\n";
            statement += "[2] View Transactions\n";
            statement += "[3] Delete Transaction\n";
            statement += "[4] Filter Transactions\n";
            statement += "[0] Exit";

            System.out.println(statement);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleAddTransaction();
                    break;

                case 2:
                    handleListTransactions();
                    break;

                case 3:
                    handleDeleteTransaction();
                    break;

                case 4:
                    handleFilterTransactions();
                    break;

                case 0:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void handleAddTransaction() {
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
                transactionManager.getTransactions().size(),
                amount,
                LocalDate.now(),
                type,
                category,
                description
        );

        transactionManager.addTransaction(transaction);
        System.out.println("Transaction added successfully!");
    }

    private static void handleListTransactions() {
        List<Transaction> transactions = transactionManager.getTransactions();
        handleDisplayTransactions(transactions);
    }

    private static void handleDeleteTransaction() {
        handleListTransactions();
        System.out.println("Which transaction would you like to delete?");

        int transactionId = scanner.nextInt();
        boolean is_removed = transactionManager.deleteTransactionById(transactionId);

        if (is_removed) {
            System.out.printf("Transaction %o has been successfully removed", transactionId);
        }
        else {
            System.out.printf("No transaction with id %o found", transactionId);
        }

        System.out.println("\n");
    }

    private static void handleFilterTransactions() {
        String statement = "Filter transactions by:\n";
        statement += "[A] Amount\n";
        statement += "[D] Date\n";
        statement += "[T] Type\n";
        statement += "[C] Category\n";

        System.out.println(statement);

        String action = scanner.next();

        switch (action.toUpperCase()) {
            case "A":
                handleFilterTransactionsByAmount();
                break;

            case "D":
                handleFilterTransactionsByDate();
                break;

            case "T":
                handleFilterTransactionsByType();
                break;

            case "C":
                handleFilterTransactionsByCategory();
                break;

            default:
                System.out.println("Invalid option, please try again.");
        }
    }

    private static void handleFilterTransactionsByAmount() {
        System.out.println("Please provide a minimum amount:");
        double minAmount = scanner.nextDouble();

        System.out.println("Please provide a maximum amount:");
        double maxAmount = scanner.nextDouble();

        List<Transaction> transactions = transactionManager.getTransactionsByAmount(
                minAmount,
                maxAmount);

        handleDisplayTransactions(transactions);
    }

    private static void handleFilterTransactionsByDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Please provide the earliest date to filter by (YYYY-MM-DD):");
        String earliestDateString = scanner.next();
        LocalDate earliestDate = LocalDate.parse(earliestDateString, dateTimeFormatter);

        System.out.println("Please provide the latest date to filter by (YYYY-MM-DD):");
        String latestDateString = scanner.next();
        LocalDate latestDate = LocalDate.parse(latestDateString, dateTimeFormatter);

        List<Transaction> transactions = transactionManager.getTransactionsByDate(
                earliestDate,
                latestDate);

        handleDisplayTransactions(transactions);
    }

    private static void handleFilterTransactionsByType() {
        System.out.println("Please provide the type to filter by:");
        String type = scanner.next();
        List<Transaction> transactions = transactionManager.getTransactionsByType(type);
        handleDisplayTransactions(transactions);
    }

    private static void handleFilterTransactionsByCategory() {
        System.out.println("Please provide the category to filter by:");
        String category = scanner.next();
        List<Transaction> transactions = transactionManager.getTransactionsByCategory(category);
        handleDisplayTransactions(transactions);
    }

    private static void handleDisplayTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.print("\nNo transactions found");
        }
        else {
            for (Transaction transaction : transactions) {
                System.out.printf("\nID: %o | Date: %s | Type: %s | Category: %s | Amount: $%.2f | Description: %s",
                        transaction.getId(),
                        transaction.getDate(),
                        transaction.getType(),
                        transaction.getCategory(),
                        transaction.getAmount(),
                        transaction.getDescription());
            }
        }

        System.out.print("\n\n");
    }
}
