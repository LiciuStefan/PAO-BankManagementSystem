package model;

import exception.InsuficientFundsException;
import exception.InvalidAmountException;
import util.CompareAmounts;

import java.time.LocalDate;

public class PrepaidCard extends Card{

    private double balance;

    public PrepaidCard(String cardId, String cardNumber, String cardHolderName, LocalDate expiryDate, Account account, double balance) {
        super(cardId ,cardNumber, cardHolderName, expiryDate, account);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }




    @Override
    public void makePayment(double amount) {
        try{
            CompareAmounts.validateAmount(amount, this.balance);
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
        //Only withdraw from the PrepaidCard balance:
        this.balance -= amount;
        System.out.println("Payment of " + amount + " was made successfully");
    }

    @Override
    public void makeDeposit(double amount)
    {
        //Only deposit to the PrepaidCard balance:
        this.balance += amount;
        System.out.println("Deposit of " + amount + " was made successfully");
    }

    @Override
    public void makeWithdrawal(double amount)
    {
        try{
            CompareAmounts.validateAmount(amount, this.balance);
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
        //Only withdraw from the PrepaidCard balance:
        this.balance -= amount;
        System.out.println("Withdrawal of " + amount + " was made successfully");
    }

    @Override
    public void makeTransfer(double amount, Account account)
    {
        System.out.println("Cannot make a transfer from a Prepaid Card");
    }

    @Override
    public String toString()
    {
        return "PrepaidCard{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                ", balance=" + balance +
                '}';
    }

}
