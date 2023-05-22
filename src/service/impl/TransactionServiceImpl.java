package service.impl;

import exception.TransactionNotInListException;
import model.*;
import repository.TransactionRepository;
import service.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionServiceImpl {

    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(){
        transactionRepository = TransactionRepository.getInstance();
    }

    public void addTransaction(Transaction transaction){
        try{
            //transactionRepository.addEntityToFile(transaction);
            transactionRepository.addEntity(transaction);
            System.out.println("Transaction added successfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<Transaction> getTransactions(){
        return transactionRepository.getEntities();
    }

    public Transaction getTransactionById(String transactionId){
       if(this.transactionRepository.getEntities().isEmpty())
           throw new TransactionNotInListException("Transaction not in list");
         Transaction transaction = this.getTransactions().stream().filter(elem -> Objects.equals(elem.getTransactionId(), transactionId)).toList().get(0);
            if(transaction == null)
                throw new TransactionNotInListException("Transaction not in list");
            return transaction;
    }

    public void deleteTransaction(Transaction transaction){
        if(this.getTransactions().isEmpty() || !this.getTransactions().contains(transaction))
            throw new TransactionNotInListException("Transaction not in list");
        this.transactionRepository.getEntities().remove(transaction);
    }

    public void saveChanges(){
        transactionRepository.saveChanges();
    }

    public List<Transaction> getAllTransactionsThatAreOfASpecificType(String transactionType){
        List<Transaction> transactions = new ArrayList<>();
        for(Transaction transaction : this.getTransactions()){
            if(Objects.equals(transactionType, "Deposit") && transaction instanceof Deposit)
                transactions.add(transaction);
            else if(Objects.equals(transactionType, "Withdraw") && transaction instanceof Withdrawal)
                transactions.add(transaction);
            else if(Objects.equals(transactionType, "Transfer") && transaction instanceof Transfer)
                transactions.add(transaction);
            else if(Objects.equals(transactionType, "Payment") && transaction instanceof Payment)
                transactions.add(transaction);
        }
        return transactions;
    }
}
