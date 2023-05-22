package service.impl;

import exception.AccountNotInListException;
import model.*;
import repository.AccountRepository;
import service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountServiceImpl implements AccountService {

   private AccountRepository accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = AccountRepository.getInstance();
    }

    @Override
    public void addAccount(Account account) {
        try{
            //accountRepository.addEntityToFile(account);
            accountRepository.addEntity(account);
            System.out.println("Account added successfully");
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Account> getAccounts() {
        return accountRepository.getEntities();
    }

    public void saveChanges(){
        accountRepository.saveChanges();
    }

    //Getting account by id using streams:
    @Override
    public Account getAccountById(String accountId) throws AccountNotInListException {
        if(this.getAccounts().isEmpty())
            throw new AccountNotInListException("Account not in list");
        Account accounts = this.getAccounts().stream().filter(elem -> Objects.equals(elem.getAccountId(), Integer.parseInt(accountId))).toList().get(0);
        if(accounts == null)
            throw new AccountNotInListException("Account not in list");
        return accounts;
    }

    @Override
    public void deleteAccount(Account account) throws AccountNotInListException {
        if(this.getAccounts().isEmpty() || !this.getAccounts().contains(account))
            throw new AccountNotInListException("Account not in list");
        this.getAccounts().remove(account);
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
