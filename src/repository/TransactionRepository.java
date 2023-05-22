package repository;

import exception.TransactionAlreadyExistsException;
import model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.FILENAME_TRANSACTION;

public class TransactionRepository extends BaseRepository<Transaction>{

    private static int TRANSACTION_NUMBER = 0;
    private static TransactionRepository instance;
    private TransactionRepository(String filename) {
        super(filename);
        this.entities = new ArrayList<>();
        loadDatabaseFromFile();
        TRANSACTION_NUMBER = this.getEntities().size();
    }

    private TransactionRepository(){
        this.entities = new ArrayList<>();
        loadDatabase();
        TRANSACTION_NUMBER = this.getEntities().size();
    }

    public static TransactionRepository getInstance(String filename) {
        if(instance == null) {
            instance = new TransactionRepository(filename);
        }
        return instance;
    }

    public static TransactionRepository getInstance(){
        if(instance == null){
            instance = new TransactionRepository();
        }
        return instance;
    }

    public static int getTransactionNumber() {
        return TRANSACTION_NUMBER;
    }

    public static void incrementTransactionNumber() {
        TRANSACTION_NUMBER++;
    }

    public void loadDatabase(){
        this.entities = new ArrayList<>();
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM transaction");
            while(result.next()) {
                if(result.getString("type").equals("Deposit"))
                {
                    Deposit deposit = new Deposit(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );

                    entities.add(deposit);
                }
                else if(result.getString("type").equals("Withdrawal"))
                {
                    Withdrawal withdrawal = new Withdrawal(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );
                    entities.add(withdrawal);
                }
                else if(result.getString("type").equals("Payment"))
                {
                    Payment payment = new Payment(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );
                    entities.add(payment);
                }
                else if(result.getString("type").equals("Transfer"))
                {
                    Transfer transfer = new Transfer(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId"),
                            result.getInt("otherAccountId")
                    );
                    entities.add(transfer);
                }
                else
                    System.out.println(("Error loading transactions from database"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading transactions from database");
        }
    }
    void loadDatabaseFromFile() {
        try(var lines = Files.lines(Paths.get(this.getFilename()))) {
            lines.forEach(this::saveEntity);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

   void saveEntity(String line) {
        var lineSplit = line.split(",");
        String type = lineSplit[0];
        Transaction transaction = null;
        var date = lineSplit[3].split("-");
        switch(type) {
            case "Deposit" ->{
                transaction = new Deposit(
                        lineSplit[1], // transactionId
                        Double.parseDouble(lineSplit[2]), // amount
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), // date
                        lineSplit[4], // description
                        Integer.parseInt(lineSplit[5])  // accountId
                );
            }

            case "Payment" -> {
                transaction = new Payment(
                        lineSplit[1], // transactionId
                        Double.parseDouble(lineSplit[2]), // amount
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), // date
                        lineSplit[4], // description
                        Integer.parseInt(lineSplit[5])
                );// accountId
            }
            case "Transfer" -> {
                transaction = new Transfer(
                        lineSplit[1], // transactionId
                        Double.parseDouble(lineSplit[2]), // amount
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), // date
                        lineSplit[4], // description
                        Integer.parseInt(lineSplit[5]), // accountId
                        Integer.parseInt(lineSplit[6]) // otherAccountId
                );
            }
            case "Withdrawal" -> {
                transaction = new Withdrawal(
                    lineSplit[1], // transactionId
                    Double.parseDouble(lineSplit[2]), // amount
                    LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), // date
                    lineSplit[4], // description
                    Integer.parseInt(lineSplit[5])// accountId
                );
            }
        }
        entities.add(transaction);
    }

    public List<Transaction> getTransactionListForSpecifiedAccount(int accountId) {
        List<Transaction> transactionListForSpecifiedAccount = new ArrayList<>();
        for(Transaction transaction : entities) {
            if(transaction.getAccountId() == accountId) {
                transactionListForSpecifiedAccount.add(transaction);
            }
        }
        return transactionListForSpecifiedAccount;
    }

    public Transaction getTransactionByIdFromDatabase(String transactionId) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM transaction WHERE idtransaction = '" + transactionId + "'");
            if(result.next()) {
                if(result.getString("type").equals("Deposit"))
                {
                    Deposit deposit = new Deposit(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );
                    return deposit;
                }
                else if(result.getString("type").equals("Withdrawal"))
                {
                    Withdrawal withdrawal = new Withdrawal(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );
                    return withdrawal;
                }
                else if(result.getString("type").equals("Payment"))
                {
                    Payment payment = new Payment(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId")
                    );
                    return payment;
                }
                else if(result.getString("type").equals("Transfer"))
                {
                    Transfer transfer = new Transfer(
                            result.getString("idtransaction"),
                            result.getDouble("amount"),
                            result.getDate("date").toLocalDate(),
                            result.getString("description"),
                            result.getInt("accountId"),
                            result.getInt("otherAccountId")
                    );
                    return transfer;
                }
                else
                    System.out.println(("Error loading transactions from database"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addTransactionToDatabase(Transaction transaction)
    {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "INSERT INTO transaction (idtransaction, amount, date, description, type, accountId, otherAccountId) VALUES ('" +
                    transaction.getTransactionId() + "', "
                    + transaction.getAmount() + ", '"
                    + transaction.getDate() + "', '"
                    + transaction.getDescription() + "', '"
                    + transaction.getClass().getSimpleName() + "', "
                    + transaction.getAccountId() + ", "
                    + (transaction instanceof Transfer ? ((Transfer)transaction).getOtherAccountId() : null)+ ")";
            statement.executeUpdate(sql);
            this.entities.add(transaction);
            System.out.println("Transaction added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
