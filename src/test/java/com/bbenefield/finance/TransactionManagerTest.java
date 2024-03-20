package com.bbenefield.finance;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Repositories.FileTransactionRepository;
import com.bbenefield.finance.Repositories.ITransactionRepository;
import com.bbenefield.finance.Services.TransactionManager;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TransactionManagerTest {
    private ITransactionRepository transactionRepositoryMock;
    private TransactionManager transactionManager;

    @BeforeEach
    public void setup() {
        transactionRepositoryMock = mock(ITransactionRepository.class);
        transactionManager = new TransactionManager(transactionRepositoryMock);
    }

    @Test
    public void addTransaction_ExceptionTest() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        Transaction transaction = new Transaction(
                10,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck");

        doThrow(new IOException("IOException from test method"))
                .when(transactionRepositoryMock)
                .createTransaction(transaction);

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.addTransaction(transaction);
        });

        verify(transactionRepositoryMock, times(1))
                .createTransaction(transaction);

        assertTrue(exception.getMessage().contains("IOException from test method"));
    }

    @Test
    public void addTransaction_SuccessTest() throws Exception {
        Transaction transaction = new Transaction(
                10,
                LocalDate.parse("2024-03-14"),
                "Income",
                "Paycheck");

        transactionManager.addTransaction(transaction);

        verify(transactionRepositoryMock, times(1))
                .createTransaction(transaction);
    }

    @Test
    public void getTransactions_FileNotFoundExceptionTest() throws Exception {
        doThrow(new IOException("IOException from test method"))
                .when(transactionRepositoryMock)
                .getTransactions();

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.getTransactions();
        }, "Hello");

        verify(transactionRepositoryMock, times(1))
                .getTransactions();

        assertEquals("IOException from test method", exception.getMessage());
    }

    @Test
    public void getTransactions_ZeroTransactionsReturnedTest() throws Exception {
        when(transactionRepositoryMock.getTransactions())
                .thenReturn(new ArrayList<>());

        assertTrue(transactionManager.getTransactions().isEmpty());
    }

    @Test
    public void getTransactions_MultipleTransactionsReturnedTest() throws Exception {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(100.00, LocalDate.now(), "Income", "Salary"),
                new Transaction(100.00, LocalDate.now(), "Income", "Salary"));

        when(transactionRepositoryMock.getTransactions())
                .thenReturn(transactions);

        assertEquals(2, transactionManager.getTransactions().size());
    }

    @Test
    public void getTransactionsByAmountTest_IOExceptionTest() throws IOException {
        doThrow(new IOException("IOException TransactionManager::getTransactionsByAmount"))
                .when(transactionRepositoryMock)
                        .getTransactionsByAmount(anyDouble(), anyDouble());
        Exception exception = assertThrows(IOException.class, () -> transactionManager.getTransactionsByAmount(0, 0));
        assertEquals("IOException TransactionManager::getTransactionsByAmount", exception.getMessage());
    }

    @Test
    public void getTransactionsByAmountTest_ZeroTransactionsReturnedTest() throws IOException {
        when(transactionRepositoryMock.getTransactionsByAmount(anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList());

        int actual = transactionManager.getTransactionsByAmount(0, 100).size();
        assertEquals(0, actual, "getTransactionsByAmount(0, 100) Should return zero transaction");
    }

    @Test
    public void getTransactionsByAmountTest_SingleTransactionReturnedTest() throws IOException {
        List<Transaction> transactions = List.of(
                new Transaction(10.00, LocalDate.now(), "Income", "Salary"));

        when(transactionRepositoryMock.getTransactionsByAmount(anyDouble(), anyDouble()))
                .thenReturn(transactions);

        int actual = transactionManager.getTransactionsByAmount(0, 10).size();
        assertEquals(1, actual, "getTransactionsByAmount(0, 100) Should return one transaction");
    }

    @Test
    public void getTransactionsByAmountTest_MultipleTransactionsReturnedTest() throws IOException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(10.00, LocalDate.now(), "Income", "Salary"),
                new Transaction(100.00, LocalDate.now(), "Income", "Salary"));

        when(transactionRepositoryMock.getTransactionsByAmount(anyDouble(), anyDouble()))
                .thenReturn(transactions);

        int actual = transactionManager.getTransactionsByAmount(0, 100).size();
        assertEquals(2, actual, "getTransactionsByAmount(0, 100) Should return two transaction");
    }

//    @Test
//    public void getTransactionsByDateTest() {
//        Transaction transaction1 = new Transaction(
//                0,
//                10,
//                LocalDate.parse("2024-03-02"),
//                "Income",
//                "Paycheck");
//
//        Transaction transaction2 = new Transaction(
//                0,
//                20,
//                LocalDate.parse("2024-03-14"),
//                "Income",
//                "Paycheck");
//
//        transactionManager.addTransaction(transaction1);
//        transactionManager.addTransaction(transaction2);
//
//        int actual1 = transactionManager.getTransactionsByDate(
//                LocalDate.parse("2024-03-02"),
//                LocalDate.parse("2024-03-03"))
//                    .size();
//        assertEquals(1, actual1, "Should return one transaction");
//
//        int actual2 = transactionManager.getTransactionsByDate(
//                LocalDate.parse("2024-03-01"),
//                LocalDate.parse("2024-03-14"))
//                    .size();
//        assertEquals(2, actual2, "Should return two transactions");
//
//        int actual3 = transactionManager.getTransactionsByDate(
//                LocalDate.parse("1970-01-01"),
//                LocalDate.parse("1970-01-01"))
//                    .size();
//        assertEquals(0, actual3, "Should return zero transaction");
//    }
//
//    @Test
//    public void getTransactionsByTypeTest() {
//        Transaction transaction1 = new Transaction(
//                0,
//                10,
//                LocalDate.parse("2024-03-02"),
//                "Income",
//                "Paycheck");
//
//        Transaction transaction2 = new Transaction(
//                0,
//                20,
//                LocalDate.parse("2024-03-14"),
//                "Withdraw",
//                "Paycheck");
//
//        Transaction transaction3 = new Transaction(
//                0,
//                20,
//                LocalDate.parse("2024-03-14"),
//                "Withdraw",
//                "Paycheck");
//
//        transactionManager.addTransaction(transaction1);
//        transactionManager.addTransaction(transaction2);
//        transactionManager.addTransaction(transaction3);
//
//        int actual1 = transactionManager.getTransactionsByType("Income").size();
//        assertEquals(1, actual1, "Should return one transaction");
//
//        int actual2 = transactionManager.getTransactionsByType("Withdraw").size();
//        assertEquals(2, actual2, "Should return two transactions");
//
//        int actual3 = transactionManager.getTransactionsByType("foobar").size();
//        assertEquals(0, actual3, "Should return zero transaction");
//    }
//
//    @Test
//    public void getTransactionsByCategoryTest() {
//        Transaction transaction1 = new Transaction(
//                0,
//                10,
//                LocalDate.parse("2024-03-02"),
//                "Income",
//                "Paycheck");
//
//        Transaction transaction2 = new Transaction(
//                0,
//                20,
//                LocalDate.parse("2024-03-14"),
//                "Withdraw",
//                "Grocery");
//
//        Transaction transaction3 = new Transaction(
//                0,
//                20,
//                LocalDate.parse("2024-03-14"),
//                "Withdraw",
//                "Grocery");
//
//        transactionManager.addTransaction(transaction1);
//        transactionManager.addTransaction(transaction2);
//        transactionManager.addTransaction(transaction3);
//
//        int actual1 = transactionManager.getTransactionsByCategory("Paycheck").size();
//        assertEquals(1, actual1, "Should return one transaction");
//
//        int actual2 = transactionManager.getTransactionsByCategory("Grocery").size();
//        assertEquals(2, actual2, "Should return two transactions");
//
//        int actual3 = transactionManager.getTransactionsByCategory("foobar").size();
//        assertEquals(0, actual3, "Should return zero transaction");
//    }
//
//    @Test
//    public void deleteTransactionByIdTest() {
//        Transaction transaction = new Transaction(
//                0,
//                10,
//                LocalDate.parse("2024-03-02"),
//                "Income",
//                "Paycheck");
//
//        transactionManager.addTransaction(transaction);
//
//        int actual1 = transactionManager.getTransactions().size();
//        assertEquals(1, actual1, "Should return one transaction");
//
//        transactionManager.deleteTransactionById(0);
//
//        int actual2 = transactionManager.getTransactions().size();
//        assertEquals(0, actual2, "Should return two transactions");
//    }

}
