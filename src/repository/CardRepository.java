package repository;

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
import java.util.Optional;

import static constants.Constants.FILENAME_ACCOUNT;
import static constants.Constants.FILENAME_CARD;

public class CardRepository extends BaseRepository<Card>{
    private static CardRepository instance;

    /*
    * Use
    * accountRepository = AccountRepository.getInstance(FILENAME_ACCOUNT);
    * if you want to use the json file
     */
    private AccountRepository accountRepository = AccountRepository.getInstance();

    private CardRepository(String filename) {
        super(filename);
        this.entities = new ArrayList<>();
        loadDatabaseFromFile();
    }

    private CardRepository() {
        this.entities = new ArrayList<>();
        loadDatabase();
    }

    public static CardRepository getInstance(String filename) {
        if(instance == null) {
            instance = new CardRepository(filename);
        }
        return instance;
    }

    public static CardRepository getInstance() {
        if(instance == null) {
            instance = new CardRepository();
        }
        return instance;
    }

    public void loadDatabase() {
        this.entities = new ArrayList<>();
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM card");
            while(result.next())
            {
                String type = result.getString("type");
                switch (type)
                {
                    case "CreditCard" -> {
                        CreditCard creditCard = new CreditCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getDouble("creditLimit"),
                                result.getDouble("balance")
                        );
                        this.entities.add(creditCard);
                    }
                    case "DebitCard" -> {
                        DebitCard debitCard = new DebitCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId"))
                        );
                        this.entities.add(debitCard);

                    }
                    case "GiftCard" -> {
                        GiftCard giftCard = new GiftCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getDouble("amountl")
                        );
                        this.entities.add(giftCard);

                    }
                    case "PrepaidCard" -> {
                        PrepaidCard prepaidCard = new PrepaidCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getDouble("balance")
                        );
                        this.entities.add(prepaidCard);


                    }
                    case "VirtualCard" -> {

                        VirtualCard virtualCard = new VirtualCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getString("securityCode")
                        );
                        this.entities.add(virtualCard);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading database");
        }
    }
    void loadDatabaseFromFile() {
        this.entities = new ArrayList<>();
        try(var lines = Files.lines(Paths.get(this.getFilename()))) {
            lines.forEach(this::saveEntity);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void saveEntity(String line) {
        var lineSplit = line.split(",");
        //1 - CreditCard, 2 - DebitCard, 3- GiftCard, 4 - PrepaidCard, 5 - VirtualCard
        String type = lineSplit[0];
        Card card = null;
        switch (type) {
            case "CreditCard" -> {
                var date = lineSplit[5].split("-");
                card = new CreditCard(
                        lineSplit[1], //id
                        Integer.parseInt(lineSplit[2]), //customerId
                        lineSplit[3], //cardNumber
                        lineSplit[4], //cvv
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), //expiryDate
                        accountRepository.getAccountById(Integer.parseInt(lineSplit[6])), //account
                        Double.parseDouble(lineSplit[7]), //creditLimit
                        Double.parseDouble(lineSplit[8])
                ); //balance
            }
            case "DebitCard" -> {
                var date  = lineSplit[5].split("-");
                card = new DebitCard(
                        lineSplit[1], //id
                        Integer.parseInt(lineSplit[2]), //customerId
                        lineSplit[3], //cardNumber
                        lineSplit[4], //cvv
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), //expiryDate
                        accountRepository.getAccountById(Integer.parseInt(lineSplit[6]))
                );

            }
            case "GiftCard" -> {
                var date = lineSplit[5].split("-");
                card = new GiftCard(
                        lineSplit[1], //id
                        Integer.parseInt(lineSplit[2]), //customerId
                        lineSplit[3], //cardNumber
                        lineSplit[4], //cvv
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), //expiryDate
                        accountRepository.getAccountById(Integer.parseInt(lineSplit[6])),
                        Double.parseDouble(lineSplit[7]) //amount
                );
            }
            case "PrepaidCard" -> {
                var date = lineSplit[5].split("-");
                card = new PrepaidCard(
                        lineSplit[1], //id
                        Integer.parseInt(lineSplit[2]), //customerId
                        lineSplit[3], //cardNumber
                        lineSplit[4], //cvv
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), //expiryDate
                        accountRepository.getAccountById(Integer.parseInt(lineSplit[6])),
                        Double.parseDouble(lineSplit[7]) //balance
                );

            }
            case "VirtualCard" -> {
                var date = lineSplit[5].split("-");
                card = new VirtualCard(
                        lineSplit[1], //id
                        Integer.parseInt(lineSplit[2]), //customerId
                        lineSplit[3], //cardNumber
                        lineSplit[4], //cvv
                        LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])), //expiryDate
                        accountRepository.getAccountById(Integer.parseInt(lineSplit[6])),
                        lineSplit[7] //virtual security code
                );
            }
        }
        entities.add(card);
    }

    public Optional<List<Card>> getCardListForSpecifiedUser(int userId)
    {
        List<Card> cards = new ArrayList<>();
        for (Card card : entities)
        {
            if (card.getCustomerId() == userId)
            {
                cards.add(card);
            }
        }
        if(cards.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(cards);
    }

    public Optional<Card> getCardById(String id)
    {
        for (Card card : entities)
        {
            if (card.getCardId().equals(id))
            {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    public Card getCardByIdFromDatabase(String id)
    {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM card WHERE idcard = '" + id + "'");

            if(result.next()){
                String type = result.getString("type");
                switch (type)
                {
                    case "CreditCard" -> {
                        Card creditCard = new CreditCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountByIdFromDatabase(result.getInt("accountId")),
                                result.getDouble("creditLimit"),
                                result.getDouble("balance")
                        );
                        return creditCard;
                    }
                    case "DebitCard" -> {
                        DebitCard debitCard = new DebitCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId"))
                        );
                        return debitCard;
                    }
                    case "GiftCard" -> {
                        GiftCard giftCard = new GiftCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getDouble("amountl")
                        );
                        return giftCard;
                    }

                    case "PrepaidCard" -> {
                        PrepaidCard prepaidCard = new PrepaidCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getDouble("balance")
                        );
                        return prepaidCard;
                    }
                    case "VirtualCard" -> {
                        VirtualCard virtualCard = new VirtualCard(
                                result.getString("idcard"),
                                result.getInt("idcustomer"),
                                result.getString("cardNumber"),
                                result.getString("cvv"),
                                result.getDate("expirationDate").toLocalDate(),
                                accountRepository.getAccountById(result.getInt("accountId")),
                                result.getString("virtualSecurityCode")
                        );
                        return virtualCard;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            }
        } catch (SQLException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addCardToDatabase(Card card) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "INSERT INTO card (idcard, idcustomer, cardNumber, cvv, expirationDate, accountId, type, creditLimit, balance, amountl, securityCode) VALUES ('" +
                    card.getCardId() + "', " +
                    card.getCustomerId() + ", '" +
                    card.getCardNumber() + "', '" +
                    card.getCvv() + "', '" +
                    card.getExpirationDate() + "', " +
                    card.getAccount().getAccountId() + ", '" +
                    card.getClass().getSimpleName() + "', " +
                    (card instanceof CreditCard ? ((CreditCard) card).getCreditLimit() : 0) + ", " +
                    (card instanceof CreditCard ? ((CreditCard) card).getBalance() : (card instanceof PrepaidCard ? ((PrepaidCard) card).getBalance() : 0)) + ", " +
                    (card instanceof GiftCard ? ((GiftCard) card).getAmount() : 0) + ", '" +
                    (card instanceof VirtualCard ? ((VirtualCard) card).getSecurityCode() : "") + "')";
            statement.executeUpdate(sql);
            this.entities.add(card);
            System.out.println("Card added successfully");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateCardInDatabase(Card card)
    {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "UPDATE card SET " +
                    "idcustomer = " + card.getCustomerId() + ", " +
                    "cardNumber = '" + card.getCardNumber() + "', " +
                    "cvv = '" + card.getCvv() + "', " +
                    "expirationDate = '" + card.getExpirationDate() + "', " +
                    "accountId = " + card.getAccount().getAccountId() + ", " +
                    "type = '" + card.getClass().getSimpleName() + "', " +
                    "creditLimit = " + (card instanceof CreditCard ? ((CreditCard) card).getCreditLimit() : 0) + ", " +
                    "balance = " + (card instanceof CreditCard ? ((CreditCard) card).getBalance() : (card instanceof PrepaidCard ? ((PrepaidCard) card).getBalance() : 0)) + ", " +
                    "amountl = " + (card instanceof GiftCard ? ((GiftCard) card).getAmount() : 0) + ", " +
                    "securityCode = '" + (card instanceof VirtualCard ? ((VirtualCard) card).getSecurityCode() : "") + "' " +
                    "WHERE idcard = '" + card.getCardId() + "'";
            statement.executeUpdate(sql);
            System.out.println("Card updated successfully");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void removeCardFromDatabase(Card card) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            String sql = "DELETE FROM card WHERE idcard = '" + card.getCardId() + "'";
            statement.executeUpdate(sql);
            System.out.println("Card deleted successfully");
            this.entities.remove(card);
            CustomerRepository.removeCardFromCustomer(card.getCustomerId(), Integer.parseInt(card.getCardId()));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
