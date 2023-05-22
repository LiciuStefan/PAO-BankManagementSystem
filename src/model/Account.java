package model;

import repository.WriteableToCSVFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO'S: make transaction method once we have the card class done;
public abstract class Account implements WriteableToCSVFile {

    private int accountId;

    private int customerId;
    private double balance;
    private List<Transaction> transactionList;

    public Account(int accountId, int customerId, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
        this.transactionList = new ArrayList<>();
    }

    public Account(int accountId, int customerId,double balance, List<Transaction> transactionList) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
        this.transactionList = transactionList;
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
        if(transactionList == null)
            transactionList = new ArrayList<>();
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", balance=" + balance +
                ", transactionList=" + transactionList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId && customerId==account.customerId && Double.compare(account.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, customerId, balance);
    }

    public String toCSV(){
        return accountId + "," + customerId + "," + balance + ",[]";
    }
}
