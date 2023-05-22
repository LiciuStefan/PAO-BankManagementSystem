package repository;

import exception.TransactionAlreadyExistsException;
import model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.FILENAME_TRANSACTION;

public class TransactionRepository extends BaseRepository<Transaction>{

    private static int TRANSACTION_NUMBER = 0;
    private static TransactionRepository instance;
    private TransactionRepository(String filename) {
        super(filename);
        loadDatabaseFromFile();
        TRANSACTION_NUMBER = this.getEntities().size();
    }

    public static TransactionRepository getInstance() {
        if(instance == null) {
            instance = new TransactionRepository(FILENAME_TRANSACTION);
        }
        return instance;
    }

    public static int getTransactionNumber() {
        return TRANSACTION_NUMBER;
    }

    public static void incrementTransactionNumber() {
        TRANSACTION_NUMBER++;
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
}
