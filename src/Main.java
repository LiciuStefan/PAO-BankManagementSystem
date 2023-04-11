import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.CustomerNotInListException;
import exception.EmptyListException;
import model.*;
import service.CardService;
import service.CustomerService;
import service.impl.AccountServiceImpl;
import service.impl.CardServiceImpl;
import service.impl.CustomerServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws CardNotInListException, EmptyListException {

        //TEST ACCOUNT SERVICE:
        AccountServiceImpl accountService = new AccountServiceImpl();
        CheckingAccount account1 = new CheckingAccount(1, 7000, 5000);
        SavingsAccount account2 = new SavingsAccount(2, 10000, 0.05);
        CheckingAccount account3 = new CheckingAccount(3, 20000, 5000);
        SavingsAccount account4 = new SavingsAccount(4, 30000, 0.05);

        accountService.addAccount(account1);
        accountService.addAccount(account2);
        accountService.addAccount(account3);
        accountService.addAccount(account4);

        List<Account> accounts = accountService.getAccounts();
        for (Account a : accounts) {
            System.out.println(a);
        }

        try {
            Account account = accountService.getAccountById("1");
            System.out.println("Account 1: ");
            System.out.println(account);
        } catch (AccountNotInListException e) {
            System.out.println(e.getMessage());
        }

        try {
            accountService.deleteAccount(account1);
            System.out.println("Deleted account 1");
        } catch (AccountNotInListException e) {
            System.out.println(e.getMessage());
        }
        accounts = accountService.getAccounts();
        for (Account a : accounts) {
            System.out.println(a);
        }

        //TEST CARD SERVICE:
        CardServiceImpl cardService = new CardServiceImpl();
        Card card1 = new CreditCard("1", "1000", "153", LocalDate.of(2021, 12, 31), account1, account1.getOverdraftLimit(), account1.getBalance());
        Card card2 = new DebitCard("2", "2000", "254", LocalDate.of(2021, 12, 31), account2);
        Card card3 = new GiftCard("3", "3000", "355", LocalDate.now(), account3, 1000);
        Card card4 = new PrepaidCard("4", "4000", "456", LocalDate.of(2024, 1, 15), account4, 1000);
        Card card5 = new VirtualCard("5", "5000", "557", LocalDate.of(2025, 12, 16), account1, "1234567890");

        cardService.addCard(card1);
        try {
            //Checking if the payment is made although the amount is too high:
            cardService.makePayment(card1, 100);
        } catch (CardNotInListException e) {
            System.out.println(e.getMessage());
        }

        cardService.addCard(card2);
        cardService.addCard(card3);
        cardService.addCard(card4);
        cardService.addCard(card5);

        try {
            cardService.makeTransfer(card1, 1000, account2);
        } catch (CardNotInListException e) {
            System.out.println(e.getMessage());
        }

        try {
            cardService.makeDeposit(card3, 500);
        } catch (CardNotInListException e) {
            System.out.println(e.getMessage());
        }

        try {
            cardService.makeWithdrawal(card4, 500);
        } catch (CardNotInListException e) {
            System.out.println(e.getMessage());
        }

        try {
            List<Card> accountCards = cardService.getAllCardsThatBelongToSpecificAccount(account1);
            System.out.println("Cards that belong to account 1: ");
            for (Card c : accountCards) {
                System.out.println(c);
            }
        }
        catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }

        try{
            List<Card> cardsThatExpireBeforeSpecificDate = cardService.getAllCardsThatExpireBeforeSpecificDate("2023-12-31");

            System.out.println("Cards that expire before 2023-12-31: ");
            for (Card c : cardsThatExpireBeforeSpecificDate) {
                System.out.println(c);
            }
        }catch (EmptyListException e){
            System.out.println(e.getMessage());
        }

        //All transactions made for account 1:
        List<Transaction> transactions = account1.getTransactionList();

        for (Transaction t : transactions) {
            System.out.println(t.getDescription());
        }

        //TEST CUSTOMER SERVICE:
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        Customer customer1 = new Customer(1, 1, "Stefan", "Liciu", "stefanliciu@gmail.com", "123456789");
        Customer customer2 = new Customer(2, 2, "Eduard", "Sabau", "sabaueduard@gmail.com", "987654321");

        customerService.addCustomer(customer1);
        customerService.addCustomer(customer2);

        Card cardCustomer = new CreditCard("5", "1111", "178", LocalDate.of(2021, 12, 31), account1, account1.getOverdraftLimit(), account1.getBalance());

        //Adding card to customer:
        customerService.addCard(customer1, cardCustomer);

        try
        {
           Customer c = customerService.getCustomerThatHasSpecificCard(cardCustomer.getCardId());
            System.out.println(c.getFirstName());
        }catch (CustomerNotInListException e){
            System.out.println(e.getMessage());
        }

        try{
            customerService.makeTransactionOnCustomerUsingCard("5", 1000, new Deposit(1000, LocalDate.now(), "Deposit", account1));
        }catch (CustomerNotInListException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Balance: " + account1.getBalance());
        try{
            customerService.makeTransactionOnCustomerUsingCard("5", 1000, new Withdrawal(1000, LocalDate.now(), "Withdrawal", account1));
        }catch (CustomerNotInListException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Balance: " + account1.getBalance());
    }
}