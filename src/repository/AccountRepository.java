package repository;

import model.Account;
import model.CheckingAccount;
import model.SavingsAccount;
import model.Transaction;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.Constants.FILENAME_ACCOUNT;
import static constants.Constants.FILENAME_TRANSACTION;

public class AccountRepository extends BaseRepository<Account>{
    private static AccountRepository instance;

    /*
     * Use
     * transactionRepository = transactionRepository.getInstance(FILENAME_TRANSACTION);
     * if you want to use the json file
     */
    private TransactionRepository transactionRepository = TransactionRepository.getInstance();
    private AccountRepository(String filename) {
        super(filename);
        this.entities = new ArrayList<>();
        loadDatabaseFromFile();
    }

    private AccountRepository() {
        this.entities = new ArrayList<>();
        loadDatabase();
    }

    public static AccountRepository getInstance(String filename) {
        if(instance == null) {
            instance = new AccountRepository(filename);
        }
        return instance;
    }

    public static AccountRepository getInstance() {
        if(instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public void loadDatabase() {
        this.entities = new ArrayList<>();
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM account");
            while(result.next())
            {
                if(result.getString("type").equals("CheckingAccount")) {
                    CheckingAccount account = new CheckingAccount(
                            result.getInt("idaccount"),
                            result.getInt("idcustomer"),
                            result.getDouble("balance"),
                            //transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("accountId")),
                            result.getDouble("overdraftLimit")
                    );

                    List<Transaction> transactions = transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("idaccount"));
                    if(transactions.isEmpty())
                    {
                        account.setTransactionList(new ArrayList<>());
                    }
                    else
                    {
                        account.setTransactionList(transactions);
                    }
                    entities.add(account);
                }
                else if(result.getString("type").equals("SavingsAccount")) {
                    SavingsAccount account = new SavingsAccount(
                            result.getInt("idaccount"),
                            result.getInt("idcustomer"),
                            result.getDouble("balance"),
                            //transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("accountId")),
                            result.getDouble("interestrate")
                    );
                    List<Transaction> transactions = transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("idaccount"));
                    if(transactions.isEmpty())
                    {
                        account.setTransactionList(new ArrayList<>());
                    }
                    else
                    {
                        account.setTransactionList(transactions);
                    }
                    entities.add(account);
                }
                else {
                    throw new RuntimeException("Invalid account type");
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong when trying to load the database: " + e.getMessage());
        }
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

    public Account getAccountByIdFromDatabase(int id) {
        try{
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM account WHERE idaccount = " + id);
            if(result.next()) {
                if(result.getString("type").equals("CheckingAccount")) {
                    CheckingAccount account = new CheckingAccount(
                            result.getInt("idaccount"),
                            result.getInt("idcustomer"),
                            result.getDouble("balance"),
                            //transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("accountId")),
                            result.getDouble("overdraftLimit")
                    );

                    List<Transaction> transactions = transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("idaccount"));
                    if(transactions.isEmpty())
                    {
                        account.setTransactionList(new ArrayList<>());
                    }
                    else
                    {
                        account.setTransactionList(transactions);
                    }
                    return account;
                }
                else if(result.getString("type").equals("SavingsAccount")) {
                    SavingsAccount account = new SavingsAccount(
                            result.getInt("idaccount"),
                            result.getInt("idcustomer"),
                            result.getDouble("balance"),
                            //transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("accountId")),
                            result.getDouble("interestrate")
                    );
                    List<Transaction> transactions = transactionRepository.getTransactionListForSpecifiedAccount(result.getInt("idaccount"));
                    if(transactions.isEmpty())
                    {
                        account.setTransactionList(new ArrayList<>());
                    }
                    else
                    {
                        account.setTransactionList(transactions);
                    }
                    return account;
                }
                else {
                    throw new RuntimeException("Invalid account type");
                }
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addAccountToDatabase(Account account) {
        try{
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "INSERT INTO account (idaccount, idcustomer, balance, type, overdraftLimit, interestrate) VALUES (" +
                    account.getAccountId() + ", " +
                    account.getCustomerId() + ", " +
                    account.getBalance() + ", " +
                    "'" + account.getClass().getSimpleName() + "', " +
                    (account instanceof CheckingAccount ? ((CheckingAccount) account).getOverdraftLimit() : null) + ", " +
                    (account instanceof SavingsAccount ? ((SavingsAccount) account).getInterestRate() : null) + ")";
            statement.executeUpdate(sql);
            this.entities.add(account);
            System.out.println("Account added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeAccountFromDatabase(int id) {
        try{
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "DELETE FROM account WHERE idaccount = " + id;
            statement.executeUpdate(sql);
            System.out.println("Account deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateAccountInDatabase(Account account){
        try{
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "UPDATE account SET " +
                    "idcustomer = " + account.getCustomerId() + ", " +
                    "balance = " + account.getBalance() + ", " +
                    "type = '" + account.getClass().getSimpleName() + "', " +
                    "overdraftLimit = " + (account instanceof CheckingAccount ? ((CheckingAccount) account).getOverdraftLimit() : null) + ", " +
                    "interestrate = " + (account instanceof SavingsAccount ? ((SavingsAccount) account).getInterestRate() : null) + " " +
                    "WHERE idaccount = " + account.getAccountId();
            statement.executeUpdate(sql);
            System.out.println("Account updated successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
