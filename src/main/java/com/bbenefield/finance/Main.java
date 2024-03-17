package com.bbenefield.finance;

import com.bbenefield.finance.Controllers.InputController;
import com.bbenefield.finance.Models.Transaction;
import com.bbenefield.finance.Services.TransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InputController.acceptUserInput();
    }
}
