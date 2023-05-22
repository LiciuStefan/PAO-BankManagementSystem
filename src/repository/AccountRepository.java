package repository;

import model.Account;
import model.CheckingAccount;
import model.SavingsAccount;
import model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.FILENAME_ACCOUNT;

public class AccountRepository extends BaseRepository<Account>{
    private static AccountRepository instance;
    private TransactionRepository transactionRepository = TransactionRepository.getInstance();
    private AccountRepository(String filename) {
        super(filename);
        loadDatabaseFromFile();
    }

    public static AccountRepository getInstance() {
        if(instance == null) {
            instance = new AccountRepository(FILENAME_ACCOUNT);
        }
        return instance;
    }
    void loadDatabaseFromFile() {
        try(var lines = Files.lines(Paths.get(this.getFilename()))) {
            lines.forEach(this::saveEntity);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void saveEntity(String line){
        var lineSplit = line.split(",");
        String type = lineSplit[0];
        Account account = null;
        switch (type){
            case "CheckingAccount" -> {
               account = new CheckingAccount(
                       Integer.parseInt(lineSplit[1]),
                       Integer.parseInt(lineSplit[2]),
                       Double.parseDouble(lineSplit[3]),
                       transactionRepository.getTransactionListForSpecifiedAccount(Integer.parseInt(lineSplit[1])),
                       Double.parseDouble(lineSplit[5])
               );
            }
            case "SavingsAccount" -> {
                account = new SavingsAccount(
                        Integer.parseInt(lineSplit[1]),
                        Integer.parseInt(lineSplit[2]),
                        Double.parseDouble(lineSplit[3]),
                        transactionRepository.getTransactionListForSpecifiedAccount(Integer.parseInt(lineSplit[1])),
                        Double.parseDouble(lineSplit[5])
                );
            }
        }
        entities.add(account);
    }


    public Account getAccountById(int id){
        for(Account account : entities){
            if(account.getAccountId() == id){
                return account;
            }
        }
        return null;
    }
}
