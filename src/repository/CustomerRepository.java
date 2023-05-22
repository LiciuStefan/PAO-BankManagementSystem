package repository;

import exception.CustomerAlreadyExistsException;
import model.Card;
import model.Customer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.Constants.FILENAME_CARD;
import static constants.Constants.FILENAME_CUSTOMER;

public class CustomerRepository extends BaseRepository<Customer>{
    private static CustomerRepository instance;

    /*
     * Use
     * cardRepository = cardRepository.getInstance(FILENAME_card);
     * if you want to use the json file
     */
    private CardRepository cardRepository = CardRepository.getInstance();
    private CustomerRepository(String filename) {
        super(filename);
        this.entities = new ArrayList<>();
        loadDatabaseFromFile();
    }

    //Creating with data from database:
    private CustomerRepository(){
        this.entities = new ArrayList<>();
        loadDatabase();
    }

    public static CustomerRepository getInstance(String filename) {
        if(instance == null) {
            instance = new CustomerRepository(filename);
        }
        return instance;
    }

    public static CustomerRepository getInstance(){
        if(instance == null){
            instance = new CustomerRepository();
        }
        return instance;
    }

    public void loadDatabase(){
        this.entities = new ArrayList<>();
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer");
            while (result.next()) {
                Customer customer = new Customer(
                        result.getInt("identity"),
                        result.getInt("idcustomer"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("cnp"),
                        new ArrayList<>()
                );
                var cards = cardRepository.getCardListForSpecifiedUser(result.getInt("idcustomer"));
                cards.ifPresent(customer::setCardList);
                entities.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong when trying to load the database from file");
        }

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
    public Optional<Customer> getCustomerById(int customerId) {
        return entities.stream().filter(customer -> customer.getCustomerId() == customerId).findFirst();
    }

    public Optional<Customer> getCustomerByCnp(String cnp) {
        return entities.stream().filter(customer -> customer.getCnp().equals(cnp)).findFirst();
    }

    public Optional<Customer> getCustomerByIdFromDatabase(int customerId) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer WHERE idcustomer = " + customerId);
            if (result.next()) {
                Customer customer = new Customer(
                        result.getInt("identity"),
                        result.getInt("idcustomer"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("cnp"),
                        new ArrayList<>()
                );
                var cards = cardRepository.getCardListForSpecifiedUser(result.getInt("idcustomer"));
                cards.ifPresent(customer::setCardList);
                return Optional.of(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
        }

    public Optional<Customer> getCustomerByCnpFromDatabase(String cnp) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM customer WHERE cnp = " + cnp);
            if (result.next()) {
                Customer customer = new Customer(
                        result.getInt("identity"),
                        result.getInt("idcustomer"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("cnp"),
                        new ArrayList<>()
                );
                var cards = cardRepository.getCardListForSpecifiedUser(result.getInt("idcustomer"));
                cards.ifPresent(customer::setCardList);
                return Optional.of(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public void addCustomerToDatabase(Customer customer) throws CustomerAlreadyExistsException {
        if(getCustomerByCnpFromDatabase(customer.getCnp()).isEmpty()) {
            try {
                Statement statement = databaseConfiguration.getConnection().createStatement();
                statement.executeUpdate("INSERT INTO customer VALUES(" +
                        customer.getCustomerId() + "," +
                        customer.getEntityId() + ",'" +
                        customer.getFirstName() + "','" +
                        customer.getLastName() + "','" +
                        customer.getEmail() + "','" +
                        customer.getCnp() + "')");

                this.entities.add(customer);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            throw new CustomerAlreadyExistsException("Customer already exists in database");
        }
    }

    public void removeCustomerFromDatabase(Customer customer) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            statement.executeUpdate("DELETE FROM customer WHERE idcustomer = " + customer.getCustomerId());
            this.entities.remove(customer);
            System.out.println("Customer removed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCustomerInDatabase(Customer customer) {
        try {
            Statement statement = databaseConfiguration.getConnection().createStatement();
            statement.executeUpdate("UPDATE customer SET " +
                    "firstName = '" + customer.getFirstName() + "'," +
                    "lastName = '" + customer.getLastName() + "'," +
                    "email = '" + customer.getEmail() + "'," +
                    "cnp = '" + customer.getCnp() + "' " +
                    "WHERE idcustomer = " + customer.getCustomerId());
            System.out.println("Customer updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeCardFromCustomer(int customerId, int cardId){
        for(Customer customer : getInstance().entities){
            if(customer.getCustomerId() == customerId){
                for(Card card : customer.getCardList()){
                    if(Integer.parseInt(card.getCardId()) == cardId){
                        customer.getCardList().remove(card);
                        break;
                    }
                }
                break;
            }
        }
    }
}
