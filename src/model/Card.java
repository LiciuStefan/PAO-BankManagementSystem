package model;


import java.time.LocalDate;

//TODO'S: validation for the card number and CVV
public abstract class Card {

    protected String cardId;
    protected String cardNumber;
    protected String cvv;
    protected LocalDate expirationDate;

    protected Account account;

    public Card(String cardId, String cardNumber, String cvv, LocalDate expirationDate, Account account) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.account = account;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public abstract void makePayment(double amount);
    public abstract void makeWithdrawal(double amount);
    public abstract void makeTransfer(double amount, Account account);
    public abstract void makeDeposit(double amount);

    @Override
    public String toString() {
        return "Card{" +
                "cardId='" + cardId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                '}';
    }
}
