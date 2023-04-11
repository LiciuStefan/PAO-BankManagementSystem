package model;

import java.time.LocalDate;
import java.util.Objects;

//Transaction id auto-increments;
public class Transaction implements Comparable<Transaction>{

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(transactionId, that.transactionId) && Objects.equals(date, that.date) && Objects.equals(description, that.description) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, amount, date, description, account);
    }

    @Override
    public int compareTo(Transaction o) {
        //Compare by amount and then by date:
        if(this.amount == o.amount)
            return this.date.compareTo(o.date);
        else
            return Double.compare(this.amount, o.amount);
    }
}
