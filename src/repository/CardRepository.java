package repository;

import model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.Constants.FILENAME_CARD;

public class CardRepository extends BaseRepository<Card>{
    private static CardRepository instance;
    private AccountRepository accountRepository = AccountRepository.getInstance();

    private CardRepository(String filename) {
        super(filename);
        loadDatabaseFromFile();
    }

    public static CardRepository getInstance() {
        if(instance == null) {
            instance = new CardRepository(FILENAME_CARD);
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
}
