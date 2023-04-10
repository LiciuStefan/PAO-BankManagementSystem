package model;

import exception.InsuficientFundsException;
import util.CompareAmounts;

import java.time.LocalDate;

public class DebitCard extends Card{

    public DebitCard(String cardId, String cardNumber, String cvv, LocalDate expirationDate, Account account) {
        super(cardId, cardNumber, cvv, expirationDate, account);
    }
    @Override
    public void makePayment(double amount) {
        try{
            CompareAmounts.validateAmount(amount, this.getAccount().getBalance());
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
        this.getAccount().setBalance(this.getAccount().getBalance() - amount);
        this.getAccount().getTransactionList().add(new Payment(amount, LocalDate.now(), "Payment from account " + this.getAccount() + "using Debit Card " + this + " of amount " + amount, this.getAccount()));
        System.out.println("Payment of " + amount + " was made successfully");
    }

    @Override
    public void makeWithdrawal(double amount) {
        try{
            CompareAmounts.validateAmount(amount, this.getAccount().getBalance());
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
        this.getAccount().setBalance(this.getAccount().getBalance() - amount);
        this.getAccount().getTransactionList().add(new Withdrawal(amount, LocalDate.now(), "Withdrawal from account " + this.getAccount() +"using Debit Card " + this + " of amount " + amount, this.getAccount()));
        System.out.println("Withdrawal of " + amount + " was made successfully");

    }

    @Override
    public void makeTransfer(double amount, Account account) {
        try{
            CompareAmounts.validateAmount(amount, this.getAccount().getBalance());
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
        this.getAccount().setBalance(this.getAccount().getBalance() - amount);
        account.setBalance(account.getBalance() + amount);
        Transfer transfer = new Transfer(amount, LocalDate.now(), "Transfer from account " + this.getAccount() + " to account " + account +"using Debit Card " + this + " of amount " + amount, this.getAccount(), account);
        this.getAccount().getTransactionList().add(transfer);
        account.getTransactionList().add(transfer);
        System.out.println("Transfer of " + amount + " was made successfully");

    }

    @Override
    public void makeDeposit(double amount) {
        this.getAccount().setBalance(this.getAccount().getBalance() + amount);
        this.getAccount().getTransactionList().add(new Deposit(amount, LocalDate.now(), "Deposit to account " + this.getAccount() +"using Debit Card " + this + " of amount " + amount, this.getAccount()));
        System.out.println("Deposit of " + amount + " was made successfully");
    }



    @Override
    public String toString() {
        return "DebitCard{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                '}';
    }
}
