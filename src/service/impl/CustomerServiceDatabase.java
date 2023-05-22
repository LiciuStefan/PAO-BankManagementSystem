package service.impl;

import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.CustomerNotInListException;
import model.Card;
import model.Customer;
import model.Transaction;
import repository.CustomerRepository;
import service.CustomerService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static constants.Constants.FILENAME_CUSTOMER;

public class CustomerServiceDatabase implements CustomerService {

    private CustomerRepository customerRepository;
    private CardServiceDatabase cardService;

    public CustomerServiceDatabase() {
        this.customerRepository = CustomerRepository.getInstance();
        this.cardService = new CardServiceDatabase();
    }

    public void addCardToCustomer(Customer customer, Card card) {
        this.cardService.addCard(card);
        customer.getCardList().add(card);
    }

    @Override
    public void addCustomer(Customer customer) {
        try{
            //customerRepository.addEntityToFile(customer);
            customerRepository.addCustomerToDatabase(customer);
            System.out.println("Customer added successfully");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Customer> getCustomers() {
        customerRepository.loadDatabase();
        return customerRepository.getEntities();
    }

    @Override
    public Customer getCustomerById(int customerId) throws CustomerNotInListException {
        if(this.getCustomers().isEmpty())
            throw new CustomerNotInListException("Customer not in list");

        Optional<Customer> customer = customerRepository.getCustomerByIdFromDatabase(customerId);
        if(customer.isEmpty())
            throw new CustomerNotInListException("Customer not in list");
        return customer.get();
    }
    @Override
    public void deleteCustomer(Customer customer) throws CustomerNotInListException {
        if(this.getCustomers().isEmpty() || !this.getCustomers().contains(customer))
            throw new CustomerNotInListException("Customer not in list");
        customerRepository.removeCustomerFromDatabase(customer);
    }

    @Override
    public Customer getCustomerByFullName(String firstName, String lastName) {
        Customer customer = this.getCustomers().stream().filter(elem -> Objects.equals(elem.getFirstName(), firstName) && Objects.equals(elem.getLastName(), lastName)).toList().get(0);
        if(customer == null)
            throw new CustomerNotInListException("Customer not in list");
        return customer;
    }

    @Override
    public Customer getCustomerThatHasSpecificCard(String cardId) {
        List<Customer> customers = this.getCustomers().stream().toList();
        for(Customer customer : customers) {
            List<Card> cards = customer.getCardList().stream().filter(elem -> Objects.equals(elem.getCardId(), cardId)).toList();
            if(!cards.isEmpty())
                return customer;
        }
        throw new CustomerNotInListException("Customer not in list");
    }

    @Override
    public void makeTransactionOnCustomerUsingCard(String cardId, double amount, Transaction transaction) throws CustomerNotInListException {
        try
        {
            //Check if the customer exists:
            Customer customer = this.getCustomerThatHasSpecificCard(cardId);
            //Check if the card exists:
            Card card = this.cardService.getCardById(cardId);
            this.cardService.makeTransactionOnCard(cardId, amount, transaction);
        }catch (CustomerNotInListException | CardNotInListException | AccountNotInListException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public Customer getCustomerByCnp(String cnp){
        Optional<Customer> customer = customerRepository.getCustomerByCnpFromDatabase(cnp);
        return customer.orElse(null);
    }
}
