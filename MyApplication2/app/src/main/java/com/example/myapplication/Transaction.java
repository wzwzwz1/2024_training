package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Transaction implements Serializable {
    private int id;
    private String amount;
    private String date;
    private String category;
    private String note;
    private String currency;

    // Constructor without ID and currency
    public Transaction(String amount, String date, String category, String note, String currency) {
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
        this.currency = currency;
    }

    // Constructor with ID
    public Transaction(int id, String amount, String date, String category, String note, String currency) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
        this.currency = currency;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    @NonNull
    @Override
    public String toString() {
        return "Transaction{" +
                "amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", note='" + note + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

}
