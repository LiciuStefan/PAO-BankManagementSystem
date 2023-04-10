package model;


import java.time.LocalDate;

//TODO'S: validation for the card number and CVV
public abstract class Card {

    private String cardId;
    private String cardNumber;
    private String cvv;
    private LocalDate expirationDate;

    public Card(String cardId, String cardNumber, String cvv, LocalDate expirationDate) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
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

    public abstract void makePayment();
    public abstract void makeWithdrawal();
    public abstract void makeTransfer();
    public abstract void makeDeposit();

}
