package model;

import java.util.ArrayList;
import java.util.List;

//TODO'S: make transaction method once we have the card class done;
public abstract class Account {

    private int accountId;
    private double balance;
    private List<Transaction> transactionList;

    public Account(int accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactionList = new ArrayList<>();
    }

    //method that gives us the exact amount of interest that we get from the account:
    public abstract double appllyInterest();

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                ", transactionList=" + transactionList +
                '}';
    }
}
