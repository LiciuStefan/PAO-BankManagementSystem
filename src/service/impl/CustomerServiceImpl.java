package service.impl;

import exception.CardNotInListException;
import exception.CustomerNotInListException;
import model.Card;
import model.Customer;
import model.Transaction;
import service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements CustomerService {

    private List<Customer> customers;
    private CardServiceImpl cardService;

    public CustomerServiceImpl() {
        this.customers = new ArrayList<>();
        this.cardService = new CardServiceImpl();
    }

    public void addCard(Customer customer, Card card) {
        this.cardService.addCard(card);
        customer.getCardList().add(card);
    }

    @Override
    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    @Override
    public List<Customer> getCustomers() {
        return this.customers;
    }

    @Override
    public Customer getCustomerById(int customerId) throws CustomerNotInListException {
        if(this.customers.isEmpty())
            throw new CustomerNotInListException("Customer not in list");
        Customer customer = this.customers.stream().filter(elem -> Objects.equals(elem.getCustomerId(), customerId)).toList().get(0);
        if(customer == null)
            throw new CustomerNotInListException("Customer not in list");
        return customer;
    }

    @Override
    public void deleteCustomer(Customer customer) throws CustomerNotInListException {
        if(this.customers.isEmpty() || !this.customers.contains(customer))
            throw new CustomerNotInListException("Customer not in list");
        this.customers.remove(customer);
    }

    @Override
    public Customer getCustomerByFullName(String firstName, String lastName) {
        Customer customer = this.customers.stream().filter(elem -> Objects.equals(elem.getFirstName(), firstName) && Objects.equals(elem.getLastName(), lastName)).toList().get(0);
        if(customer == null)
            throw new CustomerNotInListException("Customer not in list");
        return customer;
    }

    @Override
    public Customer getCustomerThatHasSpecificCard(String cardId) {
        List<Customer> customers = this.customers.stream().toList();
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
        }catch (CustomerNotInListException | CardNotInListException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
