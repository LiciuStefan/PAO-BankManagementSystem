package service.impl;

import exception.CardNotInListException;
import exception.EmptyListException;
import model.*;
import service.CardService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardServiceImpl implements CardService {
    private List<Card> cards;

    public CardServiceImpl() {
        this.cards = new ArrayList<>();
    }

    @Override
    public void addCard(Card card) {
        this.cards.add(card);
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public Card getCardById(String cardId) throws CardNotInListException {
        if(this.cards.isEmpty())
            throw new CardNotInListException("Card not in list");
        Card cards = this.getCards().stream().filter(elem -> Objects.equals(elem.getCardId(), cardId)).toList().get(0);
        if(cards == null)
            throw new CardNotInListException("Card not in list");
        return cards;
    }

    @Override
    public List<Card> getAllCardsThatExpireBeforeSpecificDate(String date) throws EmptyListException {
        List<Card> cards = this.cards.stream().filter(elem -> elem.getExpirationDate().isBefore(LocalDate.parse(date))).toList();
        if(cards.isEmpty())
            throw new EmptyListException("No cards found");
        return cards;
    }

    @Override
    public void deleteCard(Card card) throws CardNotInListException {
        if(this.cards.isEmpty() || !this.cards.contains(card))
            throw new CardNotInListException("Card not in list");
        this.cards.remove(card);
    }

    @Override
    public List<Card> getAllCardsThatBelongToSpecificAccount(Account account) throws EmptyListException {
        List<Card> cards = this.cards.stream().filter(elem -> Objects.equals(elem.getAccount().getAccountId(), account.getAccountId())).toList();
        if(cards.isEmpty())
            throw new EmptyListException("No cards found");
        return cards;
    }

    @Override
    public void makeTransactionOnCard(String cardId, double amount, Transaction transaction) throws CardNotInListException {
        if(this.cards.isEmpty())
            throw new CardNotInListException("Card not in list");
        Card card = this.getCardById(cardId);
        if(card == null)
            throw new CardNotInListException("Card not in list");
        if(transaction instanceof Deposit)
            this.makeDeposit(card, amount);
        else if(transaction instanceof Withdrawal)
            this.makeWithdrawal(card, amount);
        else if(transaction instanceof Transfer)
            this.makeTransfer(card, amount, transaction.getAccount());
        else if(transaction instanceof Payment)
            this.makePayment(card, amount);
    }

    public void makePayment(Card card, double amount) throws CardNotInListException {
        if(this.cards.isEmpty() || !this.cards.contains(card))
            throw new CardNotInListException("Card not in list");

        card.makePayment(amount);
    }

    public void makeTransfer(Card card, double amount, Account account) throws CardNotInListException {
        if(this.cards.isEmpty() || !this.cards.contains(card))
            throw new CardNotInListException("Card not in list");

        card.makeTransfer(amount, account);
    }

    public void makeWithdrawal(Card card, double amount) throws CardNotInListException {
        if(this.cards.isEmpty() || !this.cards.contains(card))
            throw new CardNotInListException("Card not in list");

        card.makeWithdrawal(amount);
    }

    public void makeDeposit(Card card, double amount) throws CardNotInListException {
        if(this.cards.isEmpty() || !this.cards.contains(card))
            throw new CardNotInListException("Card not in list");

        card.makeDeposit(amount);
    }


}
