package com.bbenefield.finance;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Services.TransactionManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionManagerTest {

    @Test
    public void addTransactionTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck",
                "");

        transactionManager.addTransaction(transaction);

        assertEquals(
                1,
                transactionManager.getTransactions().size(),
                "transactionManager should have one transaction");
    }
    
    @Test
    public void getTransactionsByAmountTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction1 = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck",
                "");

        Transaction transaction2 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck",
                "");

        transactionManager.addTransaction(transaction1);
        transactionManager.addTransaction(transaction2);
        
        int actual1 = transactionManager.getTransactionsByAmount(0, 10).size();
        assertEquals(1, actual1, "Should return one transaction");
        
        int actual2 = transactionManager.getTransactionsByAmount(0, 100).size();
        assertEquals(2, actual2, "Should return two transaction");

        int actual3 = transactionManager.getTransactionsByAmount(100, 100).size();
        assertEquals(0, actual3, "Should return zero transaction");
    }

    @Test
    public void getTransactionsByDateTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction1 = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-02"),
                "Income",
                "Paycheck",
                "");

        Transaction transaction2 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck",
                "");

        transactionManager.addTransaction(transaction1);
        transactionManager.addTransaction(transaction2);

        int actual1 = transactionManager.getTransactionsByDate(
                LocalDate.parse("2024-03-02"),
                LocalDate.parse("2024-03-03"))
                    .size();
        assertEquals(1, actual1, "Should return one transaction");

        int actual2 = transactionManager.getTransactionsByDate(
                LocalDate.parse("2024-03-01"),
                LocalDate.parse("2024-03-14"))
                    .size();
        assertEquals(2, actual2, "Should return two transactions");

        int actual3 = transactionManager.getTransactionsByDate(
                LocalDate.parse("1970-01-01"),
                LocalDate.parse("1970-01-01"))
                    .size();
        assertEquals(0, actual3, "Should return zero transaction");
    }

    @Test
    public void getTransactionsByTypeTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction1 = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-02"),
                "Income",
                "Paycheck",
                "");

        Transaction transaction2 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Withdraw",
                "Paycheck",
                "");

        Transaction transaction3 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Withdraw",
                "Paycheck",
                "");

        transactionManager.addTransaction(transaction1);
        transactionManager.addTransaction(transaction2);
        transactionManager.addTransaction(transaction3);

        int actual1 = transactionManager.getTransactionsByType("Income").size();
        assertEquals(1, actual1, "Should return one transaction");

        int actual2 = transactionManager.getTransactionsByType("Withdraw").size();
        assertEquals(2, actual2, "Should return two transactions");

        int actual3 = transactionManager.getTransactionsByType("foobar").size();
        assertEquals(0, actual3, "Should return zero transaction");
    }

    @Test
    public void getTransactionsByCategoryTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction1 = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-02"),
                "Income",
                "Paycheck",
                "");

        Transaction transaction2 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Withdraw",
                "Grocery",
                "");

        Transaction transaction3 = new Transaction(
                0,
                20,
                LocalDate.parse("2024-03-14"),
                "Withdraw",
                "Grocery",
                "");

        transactionManager.addTransaction(transaction1);
        transactionManager.addTransaction(transaction2);
        transactionManager.addTransaction(transaction3);

        int actual1 = transactionManager.getTransactionsByCategory("Paycheck").size();
        assertEquals(1, actual1, "Should return one transaction");

        int actual2 = transactionManager.getTransactionsByCategory("Grocery").size();
        assertEquals(2, actual2, "Should return two transactions");

        int actual3 = transactionManager.getTransactionsByCategory("foobar").size();
        assertEquals(0, actual3, "Should return zero transaction");
    }

    @Test
    public void deleteTransactionByIdTest() {
        TransactionManager transactionManager = new TransactionManager();
        Transaction transaction = new Transaction(
                0,
                10,
                LocalDate.parse("2024-03-02"),
                "Income",
                "Paycheck",
                "");

        transactionManager.addTransaction(transaction);

        int actual1 = transactionManager.getTransactions().size();
        assertEquals(1, actual1, "Should return one transaction");

        transactionManager.deleteTransactionById(0);

        int actual2 = transactionManager.getTransactions().size();
        assertEquals(0, actual2, "Should return two transactions");
    }

}
