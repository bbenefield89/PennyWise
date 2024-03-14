package com.bbenefield.finance;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TransactionManager transactionManager = new TransactionManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String statement = "Choose an option: ";
            statement += "[1] Add Transaction ";
            statement += "[2] View Transactions ";
            statement += "[3] Delete Transaction ";
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

                case 0:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option, please try again!");
            }
        }
    }

    private static void handleAddTransaction() {
        transactionManager.addTransaction();
        System.out.println("Transaction added successfully!");
    }

    private static void handleListTransactions() {
        List<Transaction> transactions = transactionManager.getTransactions();

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

    private static void handleDeleteTransaction() {
        Scanner scanner = new Scanner(System.in);

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
}
