package com.bbenefield.finance;

import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Repositories.ITransactionRepository;
import com.bbenefield.finance.Services.TransactionManager;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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

        Exception exception = assertThrows(IOException.class, () -> transactionManager.getTransactions());

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
        assertEquals(2, actual, "getTransactionsByAmount(0, 100) Should return two transactions");
    }

    @Test
    public void getTransactionsByDate_IOExceptionTest() throws IOException {
        LocalDate earliestDate = LocalDate.parse("2024-03-01");
        LocalDate latestDate = LocalDate.parse("2024-03-20");
        String exceptionMessage = "IOException TransactionManager::getTransactionsByDate";

        doThrow(new IOException(exceptionMessage))
                .when(transactionRepositoryMock)
                        .getTransactionsByDate(any(LocalDate.class), any(LocalDate.class));

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.getTransactionsByDate(earliestDate, latestDate);
        });

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void getTransactionsByDate_ZeroTransactionsReturnedTest() throws IOException {
        LocalDate earliestDate = LocalDate.parse("2024-03-01");
        LocalDate latestDate = LocalDate.parse("2024-03-20");

        when(transactionRepositoryMock.getTransactionsByDate(earliestDate, latestDate))
                .thenReturn(Collections.emptyList());

        int expected = 0;
        int actual = transactionManager.getTransactionsByDate(earliestDate, latestDate).size();

        assertEquals(expected, actual, "getTransactionsByDateTest should return zero transactions");
    }

    @Test
    public void getTransactionsByDate_OneOrMoreTransactionsReturnedTest() throws IOException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(10.00, LocalDate.parse("1970-01-01"), "Income", "Salary"),
                new Transaction(100.00, LocalDate.parse("3000-01-01"), "Income", "Salary"));
        LocalDate earliestDate = LocalDate.parse("2024-03-01");
        LocalDate latestDate = LocalDate.parse("2024-03-20");

        when(transactionRepositoryMock.getTransactionsByDate(earliestDate, latestDate))
                .thenReturn(transactions);

        int expected = 2;
        int actual = transactionManager.getTransactionsByDate(earliestDate, latestDate).size();

        assertEquals(expected, actual, "getTransactionsByDateTest should return zero transactions");
    }

    @Test
    public void getTransactionsByType_IOExceptionTest() throws IOException {
        String exceptionMessage = "IOException TransactionManager::getTransactionsByType";

        doThrow(new IOException(exceptionMessage))
                .when(transactionRepositoryMock)
                    .getTransactionsByType(anyString());

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.getTransactionsByType(anyString());
        });

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void getTransactionsByType_ZeroTransactionsReturnedTest() throws IOException {
        when(transactionRepositoryMock.getTransactionsByType(anyString()))
                .thenReturn(Collections.emptyList());

        int expected = 0;
        int actual = transactionManager.getTransactionsByType(anyString()).size();

        assertEquals(expected, actual, "getTransactionsByType should return zero transactions");
    }

    @Test
    public void getTransactionsByType_OneOrMoreTransactionsReturnedTest() throws IOException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(10.00, LocalDate.parse("1970-01-01"), "Income", "Salary"),
                new Transaction(100.00, LocalDate.parse("3000-01-01"), "Income", "Salary"));

        when(transactionRepositoryMock.getTransactionsByType(anyString()))
                .thenReturn(transactions);

        int expected = 2;
        int actual = transactionManager.getTransactionsByType(anyString()).size();

        assertEquals(expected, actual, "getTransactionsByType should return zero transactions");
    }

    @Test
    public void getTransactionsByCategory_IOExceptionTest() throws IOException {
        String exceptionMessage = "IOException TransactionManager::getTransactionsByCategory";

        doThrow(new IOException(exceptionMessage))
                .when(transactionRepositoryMock)
                .getTransactionsByCategory(anyString());

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.getTransactionsByCategory(anyString());
        });

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void getTransactionsByCategory_ZeroTransactionsReturnedTest() throws IOException {
        when(transactionRepositoryMock.getTransactionsByCategory(anyString()))
                .thenReturn(Collections.emptyList());

        int expected = 0;
        int actual = transactionManager.getTransactionsByCategory(anyString()).size();

        assertEquals(expected, actual, "getTransactionsByCategory should return zero transactions");
    }

    @Test
    public void getTransactionsByCategory_OneOrMoreTransactionsReturnedTest() throws IOException {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(10.00, LocalDate.parse("1970-01-01"), "Income", "Salary"),
                new Transaction(100.00, LocalDate.parse("3000-01-01"), "Income", "Salary"));

        when(transactionRepositoryMock.getTransactionsByCategory(anyString()))
                .thenReturn(transactions);

        int expected = 2;
        int actual = transactionManager.getTransactionsByCategory(anyString()).size();

        assertEquals(expected, actual, "getTransactionsByCateogory should return zero transactions");
    }

    @Test
    public void deleteTransactionById_IOExceptionTest() throws IOException {
        String exceptionMessage = "IOException TransactionManager::deleteTransactionById";

        doThrow(new IOException(exceptionMessage))
                .when(transactionRepositoryMock)
                .getTransactionsByCategory(anyString());

        Exception exception = assertThrows(IOException.class, () -> {
            transactionManager.getTransactionsByCategory(anyString());
        });

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void deleteTransactionById_TruthyTest()
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        when(transactionRepositoryMock.deleteTransactionById(any()))
                .thenReturn(true);

        UUID id = UUID.randomUUID();
        assertTrue(transactionManager.deleteTransactionById(id));
    }

    @Test
    public void deleteTransactionById_FalsyTest()
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        when(transactionRepositoryMock.deleteTransactionById(any()))
                .thenReturn(false);

        UUID id = UUID.randomUUID();
        assertFalse(transactionManager.deleteTransactionById(id));
    }

}
