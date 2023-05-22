package model;
import exception.InsuficientFundsException;
import exception.InvalidAmountException;
import util.CompareAmounts;

import java.time.LocalDate;

public class GiftCard extends Card{

    public double amount;

    public GiftCard(String cardId, int customerId, String cardNumber, String cvv, LocalDate expiryDate, Account account, double amount) {
        super(cardId , customerId, cardNumber, cvv, expiryDate, account);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



    @Override
    public void makePayment(double amount) {
        try{
            CompareAmounts.validateAmount(amount, this.amount);
            //Only withdraw from the GiftCard amount:
            this.amount -= amount;
            System.out.println("Payment of " + amount + " was made successfully");
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void makeDeposit(double amount)
    {
        //Only deposit to the GiftCard amount:
        this.amount += amount;
        System.out.println("Deposit of " + amount + " was made successfully");
    }

    @Override
    public void makeWithdrawal(double amount)
    {
        try{
            CompareAmounts.validateAmount(amount, this.amount);
            //Only withdraw from the GiftCard amount:
            this.amount -= amount;
            System.out.println("Withdrawal of " + amount + " was made successfully");
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void makeTransfer(double amount, Account account)
    {
        System.out.println("Transfer is not allowed for GiftCard");
    }

    @Override
    public String toString()
    {
        return "GiftCard{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                ", amount=" + amount +
                '}';
    }

    @Override
    public String toCSV(){
        return "GiftCard," + super.toCSV() + "," + amount;
    }
}
