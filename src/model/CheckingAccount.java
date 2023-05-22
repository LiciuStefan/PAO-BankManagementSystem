package model;

import java.util.List;

public class CheckingAccount extends Account{

    //The max amount that you can withdraw from the account:
    private double overdraftLimit;

    public CheckingAccount(int accountId, int customerId, double balance, double overdraftLimit) {
        super(accountId, customerId, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(int accountId, int customerId, double balance, List<Transaction> transactions, double overdraftLimit) {
        super(accountId, customerId, balance, transactions);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double appllyInterest() {
        //There is no interest for this account:
        return 0;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String toCSV(){
        return "CheckingAccount," + super.toCSV() + "," + overdraftLimit;
    }
}
