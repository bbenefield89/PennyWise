package com.bbenefield.finance.Models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    @CsvBindByPosition(position = 0)
    private UUID id;

    @CsvBindByPosition(position = 4)
    private double amount;

    @CsvBindByPosition(position = 1)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate date;

    @CsvBindByPosition(position = 2)
    private String type;

    @CsvBindByPosition(position = 3)
    private String category;

    public Transaction() {}

    public Transaction(double amount, LocalDate date, String type, String category) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
