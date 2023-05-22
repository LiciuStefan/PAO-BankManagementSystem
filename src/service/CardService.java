package service;

import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.EmptyListException;
import model.Account;
import model.Card;
import model.Transaction;

import java.util.List;

public interface CardService {

    public void addCard(Card card);
    public List<Card> getCards();
    public Card getCardById(String cardId) throws CardNotInListException;
    public List<Card> getAllCardsThatExpireBeforeSpecificDate(String date) throws EmptyListException;
    public void deleteCard(Card card) throws CardNotInListException;
    public List<Card>getAllCardsThatBelongToSpecificAccount(Account account) throws EmptyListException;

    public void makeTransactionOnCard(String cardId, double amount, Transaction transaction) throws CardNotInListException, AccountNotInListException;
}
