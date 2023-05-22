package service.impl;

import exception.AccountNotInListException;
import model.*;
import repository.AccountRepository;
import service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static constants.Constants.FILENAME_ACCOUNT;

public class AccountServiceDatabase implements AccountService {


    private AccountRepository accountRepository;

    public AccountServiceDatabase() {
        this.accountRepository = AccountRepository.getInstance();
    }

    @Override
    public void addAccount(Account account) {
        try{
            //accountRepository.addEntityToFile(account);
            accountRepository.addAccountToDatabase(account);
            System.out.println("Account added successfully");
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Account> getAccounts() {
        accountRepository.loadDatabase();
        return accountRepository.getEntities();
    }

    //Getting account by id using streams:
    @Override
    public Account getAccountById(String accountId) throws AccountNotInListException {
        if(this.getAccounts().isEmpty())
            throw new AccountNotInListException("Account not in list");
        Account account = accountRepository.getAccountByIdFromDatabase(Integer.parseInt(accountId));
        if(account == null)
            throw new AccountNotInListException("Account not in list");
        return account;
    }

    @Override
    public void deleteAccount(Account account) throws AccountNotInListException {
        if(this.getAccounts().isEmpty() || !this.getAccounts().contains(account))
            throw new AccountNotInListException("Account not in list");
        accountRepository.removeAccountFromDatabase(account.getAccountId());
    }

    @Override
    public List<Account> getAllAccountsThatHaveASpecificTransactionType(String transactionType) {
        List<Account> accounts = new ArrayList<>();
        for(Account account : this.getAccounts()){
            for(Transaction transaction : account.getTransactionList()){
                if(Objects.equals(transactionType, "Deposit") && transaction instanceof Deposit)
                    accounts.add(account);
                else if(Objects.equals(transactionType, "Withdraw") && transaction instanceof Withdrawal)
                    accounts.add(account);
                else if(Objects.equals(transactionType, "Transfer") && transaction instanceof Transfer)
                    accounts.add(account);
                else if(Objects.equals(transactionType, "Payment") && transaction instanceof Payment)
                    accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public void addTransactionToAccount(Account account, Transaction transaction) throws AccountNotInListException {
        //Check if the account is in the list:
        if(!this.getAccounts().contains(account))
            throw new AccountNotInListException("Account not in list");
        account.getTransactionList().add(transaction);
    }


}
