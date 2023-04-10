package model;

import java.time.LocalDate;

public class VirtualCard extends Card{

    private String securityCode;

    public VirtualCard(String cardId, String cardNumber, String cvv, LocalDate expirationDate, Account account,  String securityCode) {
        super(cardId, cardNumber, cvv, expirationDate, account);
        this.securityCode = securityCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public void validateAmount(double amount) throws Exception
    {
        if(amount > this.getAccount().getBalance())
        {
            throw new Exception("Insufficient funds");
        }
    }

    @Override
    public void makePayment(double amount) {
        try{
            validateAmount(amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.getAccount().setBalance(this.getAccount().getBalance() - amount);
        this.getAccount().getTransactionList().add(new Payment(amount, LocalDate.now(), "Payment from account " + this.getAccount() + "using Virtual Card " + this + " of amount " + amount, this.getAccount()));
        System.out.println("Payment of " + amount + " was made successfully");
    }

    @Override
    public void makeWithdrawal(double amount) {
        try{
            validateAmount(amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.getAccount().setBalance(this.getAccount().getBalance() - amount);
        this.getAccount().getTransactionList().add(new Transaction());
        System.out.println("Withdrawal of " + amount + " was made successfully");

    }

    @Override
    public void makeTransfer(double amount, Account account) {
            try{
                 validateAmount(amount);
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            this.getAccount().setBalance(this.getAccount().getBalance() - amount);
            account.setBalance(account.getBalance() + amount);
            this.getAccount().getTransactionList().add(new Transaction());
            account.getTransactionList().add(new Transaction());
            System.out.println("Transfer of " + amount + " was made successfully");

    }

    @Override
    public void makeDeposit(double amount) {
        this.getAccount().setBalance(this.getAccount().getBalance() + amount);
        this.getAccount().getTransactionList().add(new Transaction());
        System.out.println("Deposit of " + amount + " was made successfully");
    }

    @Override
    public String toString() {
        return "VirtualCard{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                ", securityCode='" + securityCode + '\'' +
                '}';
    }
}
