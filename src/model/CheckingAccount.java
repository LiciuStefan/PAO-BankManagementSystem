package model;

public class CheckingAccount extends Account{

    //The max amount that you can withdraw from the account:
    private double overdraftLimit;

    public CheckingAccount(int accountId, double balance, double overdraftLimit) {
        super(accountId, balance);
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

}
