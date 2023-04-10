package service;

import exception.AccountNotInListException;
import model.Account;
import model.Transaction;

import java.util.List;

public interface AccountService {

    public void addAccount(Account account);
    public List<Account> getAccounts();
    public Account getAccountById(String accountId) throws AccountNotInListException;
    public void deleteAccount(Account account) throws AccountNotInListException;

    public List<Account>getAllAccountsThatHaveASpecificTransactionType(String transactionType);
    public void addTransactionToAccount(Account account, Transaction transaction) throws AccountNotInListException;
}
