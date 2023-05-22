package model;

import exception.InsuficientFundsException;
import exception.InvalidAmountException;
import util.CompareAmounts;

import java.time.LocalDate;
import java.util.Collections;

public class CreditCard extends Card{

    private double creditLimit;
    private double balance;

    public CreditCard(String cardId, int customerId, String cardNumber, String cvv, LocalDate expiryDate, Account account, double creditLimit, double balance) {
        super(cardId, customerId,cardNumber, cvv, expiryDate, account);
        this.creditLimit = creditLimit;
        this.balance = balance;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
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
            //Withdraw the amount from balance + from the account balance and add a new Transaction to the account transaction list:
            this.balance -= amount;
            this.getAccount().setBalance(this.getAccount().getBalance() - amount);
            Payment payment = new Payment(amount, LocalDate.now(), "Payment from " + this.getAccount().toString() + " using Credit Card " + this + " of amount " + amount, this.getAccount().getAccountId());
            //Add the payment to the account transaction list using binary search:
            int pos = Collections.binarySearch(this.getAccount().getTransactionList(), payment);
            if (pos < 0) {
                pos = -pos - 1;
            }
            this.getAccount().getTransactionList().add(pos, payment);
            System.out.println("Payment of " + amount + " was made successfully");
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void makeDeposit(double amount)
    {
        //Deposit the amount to balance + to the account balance and add a new Transaction to the account transaction list:
        this.balance += amount;
        this.getAccount().setBalance(this.getAccount().getBalance() + amount);
        Deposit deposit = new Deposit(amount, LocalDate.now(), "Deposit to " + this.getAccount().toString() + " using Credit Card " + this + " of amount " + amount, this.getAccount().getAccountId());
        //Add the deposit to the account transaction list using binary search:
        int pos = Collections.binarySearch(this.getAccount().getTransactionList(), deposit);
        if (pos < 0) {
            pos = -pos - 1;
        }
        this.getAccount().getTransactionList().add(pos, deposit);
        System.out.println("Deposit of " + amount + " was made successfully");
    }

    @Override
    public void makeWithdrawal(double amount)
    {
        try{
            CompareAmounts.validateAmount(amount, this.balance);
            //Withdraw the amount from balance + from the account balance and add a new Transaction to the account transaction list:
            this.balance -= amount;
            this.getAccount().setBalance(this.getAccount().getBalance() - amount);
            Withdrawal withdrawal = new Withdrawal(amount, LocalDate.now(), "Withdrawal from " + this.getAccount().toString() + " using Credit Card " + this + " of amount " + amount, this.getAccount().getAccountId());
            //Add the withdrawal to the account transaction list using binary search:
            int pos = Collections.binarySearch(this.getAccount().getTransactionList(), withdrawal);
            if (pos < 0) {
                pos = -pos - 1;
            }
            this.getAccount().getTransactionList().add(pos, withdrawal);
            System.out.println("Withdrawal of " + amount + " was made successfully");
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void makeTransfer(double amount, Account account)
    {
        try{
            CompareAmounts.validateAmount(amount, this.balance);
            //Withdraw the amount from balance + from the account balance and add a new Transaction to the account transaction list:
            this.balance -= amount;
            this.getAccount().setBalance(this.getAccount().getBalance() - amount);
            Transfer transfer = new Transfer(amount, LocalDate.now(), "Transfer from " + this.getAccount().toString() + " to " + account.toString() + " using CreditCard " + this + " of amount " + amount, this.getAccount().getAccountId(), account.getAccountId());
            int pos = Collections.binarySearch(this.getAccount().getTransactionList(), transfer);
            if (pos < 0) {
                pos = -pos - 1;
            }

            //Add the transfer to the account transaction list using binary search:
            this.getAccount().getTransactionList().add(pos, transfer);

            //Deposit the amount to the account balance and add a new Transaction to the account transaction list:
            account.setBalance(account.getBalance() + amount);
            pos = Collections.binarySearch(account.getTransactionList(), transfer);
            if (pos < 0) {
                pos = -pos - 1;
            }
            //Add the transfer to the account transaction list using binary search:
            account.getTransactionList().add(pos, transfer);

            System.out.println("Transfer of " + amount + " was made successfully");
        } catch (InsuficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public String toString() {
        return "CreditCard{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                ", creditLimit=" + creditLimit +
                ", balance=" + balance +
                '}';    }

    @Override
    public String toCSV(){
        return "CreditCard," + super.toCSV() + "," + this.creditLimit + "," + this.balance;
    }

}
