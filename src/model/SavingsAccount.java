package model;

import java.util.List;

public class SavingsAccount extends Account{

    //the rate of interest for this account (the money that you get by keeping money in the account for a period of time or the fee you pay when borrowing money)
    private double interestRate;

    public SavingsAccount(int accountId, int customerId, double balance, double interestRate) {
        super(accountId, customerId, balance);
        this.interestRate = interestRate;
    }

    public SavingsAccount(int accoutId, int customerId, double balance, List<Transaction> transactions, double interestRate) {
        super(accoutId, customerId, balance, transactions);
        this.interestRate = interestRate;
    }

    @Override
    public double appllyInterest() {
        return this.getBalance() * this.interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toCSV(){
        return "SavingsAccount," + super.toCSV() + "," + interestRate;
    }
}
