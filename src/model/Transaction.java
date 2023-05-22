package model;

import repository.WriteableToCSVFile;

import java.time.LocalDate;
import java.util.Objects;

//Transaction id auto-increments;
public class Transaction implements Comparable<Transaction>, WriteableToCSVFile {

    private static int transactionIdCounter = 0;
    private String transactionId;
    private double amount;
    private LocalDate date;
    private String description;
    private int accountId;

    public Transaction(double amount, LocalDate date, String description, int accountId) {
        transactionIdCounter++;
        this.transactionId = String.valueOf(transactionIdCounter);
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.accountId = accountId;
    }

    public Transaction(String transactionId, double amount, LocalDate date, String description, int accountId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.accountId = accountId;
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

    public int getAccountId(){
        return accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(transactionId, that.transactionId) && Objects.equals(date, that.date) && Objects.equals(description, that.description) && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, amount, date, description, accountId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", accountId=" + accountId +
                '}';
    }

    @Override
    public int compareTo(Transaction o) {
        //Compare by amount and then by date:
        if(this.amount == o.amount)
            return this.date.compareTo(o.date);
        else
            return Double.compare(this.amount, o.amount);
    }

    public String toCSV(){
        return transactionId + "," + amount + "," + date + "," + description + "," + accountId;
    }
}
