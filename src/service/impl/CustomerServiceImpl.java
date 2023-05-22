package service.impl;

import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.CustomerNotInListException;
import model.Card;
import model.Customer;
import model.Transaction;
import repository.CustomerRepository;
import service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

   private CustomerRepository customerRepository;
    private CardServiceImpl cardService;

    public CustomerServiceImpl() {
        this.customerRepository = CustomerRepository.getInstance();
        this.cardService = new CardServiceImpl();
    }

    public void addCardToCustomer(Customer customer, Card card) {
        this.cardService.addCard(card);
        customer.getCardList().add(card);
    }

    @Override
    public void addCustomer(Customer customer) {
       try{
                //customerRepository.addEntityToFile(customer);
           customerRepository.addEntity(customer);
           System.out.println("Customer added successfully");
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.getEntities();
    }

    @Override
    public Customer getCustomerById(int customerId) throws CustomerNotInListException {
        if(this.getCustomers().isEmpty())
            throw new CustomerNotInListException("Customer not in list");
        Customer customer = this.getCustomers().stream().filter(elem -> Objects.equals(elem.getCustomerId(), customerId)).toList().get(0);
        if(customer == null)
            throw new CustomerNotInListException("Customer not in list");
        return customer;
    }

    public void saveChanges(){
        customerRepository.saveChanges();
    }
    @Override
    public void deleteCustomer(Customer customer) throws CustomerNotInListException {
        if(this.getCustomers().isEmpty() || !this.getCustomers().contains(customer))
            throw new CustomerNotInListException("Customer not in list");
        this.getCustomers().remove(customer);
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
        Optional<Customer> customer = customerRepository.getCustomerByCnp(cnp);
        return customer.orElse(null);
    }
}
