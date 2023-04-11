package service;

import exception.CustomerNotInListException;
import model.Customer;
import model.Transaction;

import java.util.List;

public interface CustomerService {

    public void addCustomer(Customer customer);
    public List<Customer> getCustomers();
    public Customer getCustomerById(int customerId) throws CustomerNotInListException;
    public void deleteCustomer(Customer customer) throws CustomerNotInListException;
    public Customer getCustomerByFullName(String firstName, String lastName);
    public Customer getCustomerThatHasSpecificCard(String cardId);
    public void makeTransactionOnCustomerUsingCard(String cardId, double amount, Transaction transaction) throws CustomerNotInListException;
}
