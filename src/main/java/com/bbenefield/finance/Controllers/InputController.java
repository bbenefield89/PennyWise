package com.bbenefield.finance.Controllers;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Repositories.FileTransactionRepository;
import com.bbenefield.finance.Repositories.ITransactionRepository;
import com.bbenefield.finance.Repositories.LedgerRepository;
import com.bbenefield.finance.Services.TransactionManager;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InputController {
    private static ITransactionRepository transactionRepository;
    private static TransactionManager transactionManager;
    private static final Scanner scanner = new Scanner(System.in);

    public static void promptLedgerSelection() {
        boolean isLedgerSelected = false;

        while (!isLedgerSelected) {
            String statement = "Would you like to:\n";
            statement += "[L]oad an existing ledger\n";
            statement += "[C]reate a new ledger\n";
            System.out.println(statement);

            String choice = scanner.next();

            switch (choice.toUpperCase()) {
                case "L":
                    isLedgerSelected = handleLoadExistingLedger();
                    break;

                case "C":
                    isLedgerSelected = handleCreateNewLedger();
                    break;

                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }

    private static boolean handleLoadExistingLedger() {
        String statement = "\nPlease provide the file name of your ledger (e.g my_ledger.csv)";
        System.out.println(statement);
        String ledgerFileName = scanner.next();

        if (LedgerRepository.checkLedgerExistence(ledgerFileName)) {
            transactionRepository = new FileTransactionRepository(ledgerFileName);
            transactionManager = new TransactionManager(transactionRepository);
            return true;
        }

        System.out.println("\nLedger named " + ledgerFileName + " not found\n");
        return false;
    }

    private static boolean handleCreateNewLedger() {
        String statement = "\nPlease provide a name for your new ledger (e.g. my_ledger.csv)";
        System.out.println(statement);
        String ledgerFileName = scanner.next();

        try {
            LedgerRepository.createLedger(ledgerFileName);
            transactionRepository = new FileTransactionRepository(ledgerFileName);
            transactionManager = new TransactionManager(transactionRepository);
            return true;
        }
        catch (IOException e) {
            System.out.println("\nError creating ledger: " + e.getMessage() + "\n");
        }

        return false;
    }

    public static void acceptUserInput() {
        while (true) {
            try {
                String statement = "\nChoose an option:\n";
                statement += "[A] Add Transaction\n";
                statement += "[V] View Transactions\n";
                statement += "[D] Delete Transaction\n";
                statement += "[F] Filter Transactions\n";
                statement += "[E] Exit\n";

                System.out.println(statement);
                String choice = scanner.next();

                switch (choice.toUpperCase()) {
                    case "A":
                        handleAddTransaction();
                        break;

                    case "V":
                        handleListTransactions();
                        break;

                    case "D":
                        handleDeleteTransaction();
                        break;

                    case "F":
                        handleFilterTransactions();
                        break;

                    case "E":
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (InputMismatchException e) {
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

        Transaction transaction = new Transaction(amount, LocalDate.now(), type, category);

        try {
            transactionManager.addTransaction(transaction);
        }
        catch (CsvRequiredFieldEmptyException e) {
            System.out.println("A required fields in the CSV file is empty: " + e.getMessage());
        }
        catch (CsvDataTypeMismatchException e) {
            System.out.println("There is a data type mismatch in the CSV: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("An error occurred while reading/writing CSV: " + e.getMessage());
        }
    }

    private static void handleListTransactions() {
        try {
            List<Transaction> transactions = transactionManager.getTransactions();
            handleDisplayTransactions(transactions);
        } catch (IOException e) {
            System.out.println("An error occurred while reading/writing CSV: " + e.getMessage());
        }
    }

    private static void handleDeleteTransaction() {
        handleListTransactions();
        System.out.println("Which transaction would you like to delete?");

        String transactionIdString = scanner.next();
        UUID transactionId = UUID.fromString(transactionIdString);

        try {
            boolean is_removed = transactionManager.deleteTransactionById(transactionId);

            if (is_removed) {
                System.out.printf("Transaction %s has been successfully removed", transactionId);
            }
            else {
                System.out.printf("No transaction with id %s found", transactionId);
            }
        }
        catch (CsvRequiredFieldEmptyException e) {
            System.out.println("A required fields in the CSV file is empty: " + e.getMessage());
        }
        catch (CsvDataTypeMismatchException e) {
            System.out.println("There is a data type mismatch in the CSV: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("An error occurred while reading/writing CSV: " + e.getMessage());
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
        try {
            System.out.println("Please provide a minimum amount:");
            double minAmount = scanner.nextDouble();

            System.out.println("Please provide a maximum amount:");
            double maxAmount = scanner.nextDouble();

            List<Transaction> transactions = transactionManager.getTransactionsByAmount(minAmount, maxAmount);
            handleDisplayTransactions(transactions);
        }
        catch (IOException e) {
            System.out.println("Error filtering transactions by amount: " + e.getMessage());
        }
    }

    private static void handleFilterTransactionsByDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Please provide the earliest date to filter by (YYYY-MM-DD):");
        String earliestDateString = scanner.next();
        LocalDate earliestDate = LocalDate.parse(earliestDateString, dateTimeFormatter);

        System.out.println("Please provide the latest date to filter by (YYYY-MM-DD):");
        String latestDateString = scanner.next();
        LocalDate latestDate = LocalDate.parse(latestDateString, dateTimeFormatter);

        try {
            List<Transaction> transactions = transactionManager.getTransactionsByDate(earliestDate, latestDate);
            handleDisplayTransactions(transactions);
        } catch (IOException e) {
            System.out.println("Error filtering transactions by date: " + e.getMessage());
        }
    }

    private static void handleFilterTransactionsByType() {
        System.out.println("Please provide the type to filter by:");
        String type = scanner.next();
        try {
            List<Transaction> transactions = transactionManager.getTransactionsByType(type);
            handleDisplayTransactions(transactions);
        } catch (IOException e) {
            System.out.println("Error filtering transactions by type: " + e.getMessage());
        }
    }

    private static void handleFilterTransactionsByCategory() {
        System.out.println("Please provide the category to filter by:");
        String category = scanner.next();

        try {
            List<Transaction> transactions = transactionManager.getTransactionsByCategory(category);
            handleDisplayTransactions(transactions);
        }
        catch (IOException e) {
            System.out.println("Error filtering transactions by type: " + e.getMessage());
        }
    }

    private static void handleDisplayTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.print("\nNo transactions found");
        }
        else {
            for (Transaction transaction : transactions) {
                System.out.printf("\nID: %s | Date: %s | Type: %s | Category: %s | Amount: $%.2f",
                        transaction.getId(),
                        transaction.getDate(),
                        transaction.getType(),
                        transaction.getCategory(),
                        transaction.getAmount());
            }
        }

        System.out.print("\n");
    }
}
