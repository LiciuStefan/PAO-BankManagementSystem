package service;

import exception.TransactionNotInListException;
import model.Transaction;

import java.util.List;

public interface TransactionService {

    public void addTransaction(Transaction transaction);
    public List<Transaction> getTransactions();
    public Transaction getTransactionById(String transactionId) throws TransactionNotInListException;
    public void deleteTransaction(Transaction transaction) throws TransactionNotInListException;
    public List<Transaction> getAllTransactionsThatAreOfASpecificType(String transactionType);

}
