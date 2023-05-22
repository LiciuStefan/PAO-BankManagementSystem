package repository;

import exception.CustomerAlreadyExistsException;
import model.Card;
import model.Customer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.Constants.FILENAME_CUSTOMER;

public class CustomerRepository extends BaseRepository<Customer>{
    private static CustomerRepository instance;
    private CardRepository cardRepository = CardRepository.getInstance();
    private CustomerRepository(String filename) {
        super(filename);
        loadDatabaseFromFile();
    }

    public static CustomerRepository getInstance() {
        if(instance == null) {
            instance = new CustomerRepository(FILENAME_CUSTOMER);
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

    //Nu vreau sa adaug cardurile in customer.csv, ci doar sa le iau din lista de carduri (ulterior din card.csv):
    void saveEntity(String line) {
        var lineSplit = line.split(",");
        Optional<List<Card>> cardList = cardRepository.getCardListForSpecifiedUser(Integer.parseInt(lineSplit[1]));
        Customer customer;
        if(cardList.isEmpty()){
            customer = new Customer(
                    Integer.parseInt(lineSplit[0]),
                    Integer.parseInt(lineSplit[1]),
                    lineSplit[2],
                    lineSplit[3],
                    lineSplit[4],
                    lineSplit[5],
                    new ArrayList<>()
            );
        }
        else {
            customer = new Customer(
                    Integer.parseInt(lineSplit[0]),
                    Integer.parseInt(lineSplit[1]),
                    lineSplit[2],
                    lineSplit[3],
                    lineSplit[4],
                    lineSplit[5],
                    cardList.get()
                    //cardRepository.getCardListForSpecifiedUser(Integer.parseInt(lineSplit[1]))
            );
        }
        entities.add(customer);
        }

    //Returns an Optional, need to check if it's null or not:
    public Optional<?> getCustomerById(int customerId) {
        return entities.stream().filter(customer -> customer.getCustomerId() == customerId).findFirst();
    }

    public Optional<Customer> getCustomerByCnp(String cnp) {
        return entities.stream().filter(customer -> customer.getCnp().equals(cnp)).findFirst();
    }
}
