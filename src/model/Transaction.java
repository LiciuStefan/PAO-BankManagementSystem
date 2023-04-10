package model;

import java.time.LocalDate;

//Transaction id auto-increments;
public class Transaction {

    private static int transactionIdCounter = 0;
    private String transactionId;
    private double amount;
    private LocalDate date;
    private String description;
    private Account account;

    public Transaction(double amount, LocalDate date, String description, Account account) {
        transactionIdCounter++;
        this.transactionId = String.valueOf(transactionIdCounter);
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.account = account;
    }

    public Transaction(){

    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
