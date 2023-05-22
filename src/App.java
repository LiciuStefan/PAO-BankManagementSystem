import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.CustomerNotInListException;
import model.*;
import repository.AuditRepository;
import repository.TransactionRepository;
import service.impl.*;
import util.KeyPad;
import util.Screen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class App {

    private final KeyPad keyPad;
    private final Screen screen;
    private final AccountServiceImpl accountService;
    private final CardServiceImpl cardService;
    private final CustomerServiceImpl customerService;
    private final TransactionServiceImpl transactionService;

    private final AuditRepository auditRepository = AuditRepository.getInstance();

    private final AuditServiceImpl auditService = new AuditServiceImpl(auditRepository);

    private BankEntity currentUser; //Administrator, Customer

    public App(KeyPad keyPad, Screen screen, AccountServiceImpl accountService, CardServiceImpl cardService, CustomerServiceImpl customerService, TransactionServiceImpl transactionService) {
        this.keyPad = keyPad;
        this.screen = screen;
        this.accountService = accountService;
        this.cardService = cardService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                login();
                continue;
            }
            if (currentUser instanceof Administrator) {
                screen.displayMessage("Welcome, Administrator!");
                screen.displayAdministratorMenu();
                var chosenAction = keyPad.getAdministratorAction();
                if (!executeAdministratorAction(chosenAction)) {
                    return;
                }
            } else {
                screen.displayMenu();
                var chosenAction = keyPad.getAction();

                if (!executeAction(chosenAction)) {
                    return;
                }
            }
        }
    }

    private boolean executeAction(KeyPad.Action action) {
        switch (action) {
            case VIEW -> {
                viewBalance();
            }
            case DEPOSIT -> {
                deposit();
            }
            case WITHDRAWAL -> {
                withdrawal();
            }
            case TRANSFER -> {
                transfer();
            }
            case PAYMENT -> {
                payment();
            }
            case LOGOUT -> {
                currentUser = null;
                //Need to save the changes into the csv files:
                accountService.saveChanges();
                customerService.saveChanges();
                cardService.saveChanges();
                transactionService.saveChanges();
            }
            case EXIT -> {
                accountService.saveChanges();
                customerService.saveChanges();
                cardService.saveChanges();
                transactionService.saveChanges();
                return false;
            }
        }

        return true;
    }

    private void transfer() {
        screen.displayMessage("Choose the account from which you want to make the transfer:");
        Optional<Account> account = Optional.ofNullable(chooseAccount());
        if(account.isEmpty())
            return;
        screen.displayMessage("Choose the account to which you want to make the transfer:");
        Optional<Account> accountToTransfer = Optional.ofNullable(chooseAccount());
        if(accountToTransfer.isEmpty())
            return;
        try {
            screen.displayMessage("Enter the amount you want to transfer:");
            var amount = keyPad.readString();
            if(Double.parseDouble(amount) <= 0)
            {
                throw new IllegalArgumentException("The amount must be positive!");
            }
            if(account.get().getBalance() < Double.parseDouble(amount))
            {
                throw new IllegalArgumentException("You don't have enough money in your account!");
            }
            //ne folosim de makeTransactionOnCard din CardServiceImpl
            //Alegem cardul - trebuie sa aiba acelasi account cu cel calculat mai sus:
            var cards = cardService.getCards().stream().filter(c -> c.getAccount().getAccountId() == account.get().getAccountId()).toList();
            if(cards.isEmpty())
            {
                screen.displayMessage("You don't have any cards for this account!");
                return;
            }
            //Alegem cardul:
            screen.displayMessage("Choose the card you want to use:");
            cards.forEach(card -> screen.displayMessage(card.toString()));
            screen.displayMessage("Please enter the card's id:");
            var cardId = keyPad.readString();
            var card = cards.stream().filter(c -> Objects.equals(c.getCardId(), cardId)).findFirst();
            if(card.isEmpty())
            {
                screen.displayMessage("You don't have a card with this id!");
                return;
            }

            if(card.get() instanceof GiftCard)
            {
                screen.displayMessage("You can't make a transfer with a gift card!");
                return;
            }
            if(card.get() instanceof PrepaidCard)
            {
                screen.displayMessage("You can't make a transfer with a prepaid card!");
                return;
            }
            //Alegem cardul destinatie:
            screen.displayMessage("Choose the card you want to transfer to:");
            var cardsToTransfer = cardService.getCards().stream().filter(c -> c.getAccount().getAccountId() == accountToTransfer.get().getAccountId()).toList();
            if(cardsToTransfer.isEmpty())
            {
                screen.displayMessage("The account you want to transfer to doesn't have any cards!");
                return;
            }
            cardsToTransfer.forEach(cardToTransfer -> screen.displayMessage(cardToTransfer.toString()));
            screen.displayMessage("Please enter the card's id:");
            var cardIdToTransfer = keyPad.readString();
            var cardToTransfer = cardsToTransfer.stream().filter(c -> Objects.equals(c.getCardId(), cardIdToTransfer)).findFirst();
            if(cardToTransfer.isEmpty())
            {
                screen.displayMessage("The account you want to transfer to doesn't have a card with this id!");
                return;
            }
            if(cardToTransfer.get() instanceof GiftCard)
            {
                screen.displayMessage("You can't make a transfer to a gift card!");
                return;
            }

            if(cardToTransfer.get() instanceof PrepaidCard)
            {
                screen.displayMessage("You can't make a transfer to a prepaid card!");
                return;
            }

            //Facem tranzactia:
            screen.displayMessage("Please enter a description for the transaction:");
            var description = keyPad.readLine();
            TransactionRepository.incrementTransactionNumber();
            Transfer transfer = new Transfer(String.valueOf(TransactionRepository.getTransactionNumber()),Double.parseDouble(amount), LocalDate.now(), description,accountToTransfer.get().getAccountId(),account.get().getAccountId());
            cardService.makeTransactionOnCard(card.get().getCardId(), Double.parseDouble(amount), transfer);
            //Should add the transaction to the list of transactions of the account:
            cardToTransfer.get().getAccount().getTransactionList().add(transfer);
            if(cardToTransfer.get() instanceof CreditCard)
            {
                //Should set the new balance for the credit card:
                ((CreditCard) cardToTransfer.get()).setBalance(((CreditCard) cardToTransfer.get()).getBalance() + Double.parseDouble(amount));
            }
            cardService.makeTransactionOnCard(cardToTransfer.get().getCardId(), Double.parseDouble(amount), transfer);
            //Adaugam tranzactia in lista de tranzactii:
            transactionService.addTransaction(transfer);
            screen.displayMessage("Transfer made successfully!");
            auditService.addCommandToFile("Timestamp: " + LocalDate.now() + " - Transfer made from account " + account.get().getAccountId() + " with the amount of " + amount + " RON to account " + accountToTransfer.get().getAccountId() + " with the description " + description);
        } catch (AccountNotInListException | CardNotInListException | IllegalArgumentException e) {
            screen.displayMessage(e.getMessage());
        }
    }

    private void payment() {
        screen.displayMessage("Choose the account from which you want to make the payment:");
        Optional<Account> account = Optional.ofNullable(chooseAccount());
        if(account.isEmpty())
            return;
        try {
            screen.displayMessage("Enter the amount you want to transfer:");
            var amount = keyPad.readString();
            if(Double.parseDouble(amount) <= 0)
            {
                throw new IllegalArgumentException("The amount must be positive!");
            }
            if(account.get().getBalance() < Double.parseDouble(amount))
            {
                throw new IllegalArgumentException("You don't have enough money in your account!");
            }
            //ne folosim de makeTransactionOnCard din CardServiceImpl
            //Alegem cardul - trebuie sa aiba acelasi account cu cel calculat mai sus:
            var cards = cardService.getCards().stream().filter(c -> c.getAccount().getAccountId() == account.get().getAccountId()).toList();
            if(cards.isEmpty())
            {
                screen.displayMessage("You don't have any cards for this account!");
                return;
            }
            //Alegem cardul:
            screen.displayMessage("Choose the card you want to use:");
            cards.forEach(card -> screen.displayMessage(card.toString()));
            screen.displayMessage("Please enter the card's id:");
            var cardId = keyPad.readString();
            var card = cards.stream().filter(c -> Objects.equals(c.getCardId(), cardId)).findFirst();
            if(card.isEmpty())
            {
                screen.displayMessage("You don't have a card with this id!");
                return;
            }
            //Facem tranzactia:
            screen.displayMessage("Please enter a description for the transaction:");
            var description = keyPad.readLine();
            TransactionRepository.incrementTransactionNumber();
            Payment payment = new Payment(String.valueOf(TransactionRepository.getTransactionNumber()),Double.parseDouble(amount), LocalDate.now(), description,account.get().getAccountId());
            cardService.makeTransactionOnCard(card.get().getCardId(), Double.parseDouble(amount), payment);
            //Adaugam tranzactia in lista de tranzactii:
            transactionService.addTransaction(payment);
            screen.displayMessage("Payment made successfully!");
            auditService.addCommandToFile("Timestamp: " + LocalDateTime.now() + " - Payment made on account " + account.get().getAccountId() + " with the amount of " + amount + " RON");

        } catch (IllegalArgumentException | AccountNotInListException | CardNotInListException e) {
            screen.displayMessage(e.getMessage());
        }
    }
    private void withdrawal() {
        screen.displayMessage("Choose the account from which you want to withdraw:");
        Optional<Account> account = Optional.ofNullable(chooseAccount());
        if(account.isEmpty())
            return;
        try {
            screen.displayMessage("Enter the amount you want to withdraw:");
            var amount = keyPad.readString();
            if(Double.parseDouble(amount) <= 0)
            {
                throw new IllegalArgumentException("The amount must be positive!");
            }
            //Should also check if the account has the necessary amount of money:
            if(account.get().getBalance() < Double.parseDouble(amount))
            {
                throw new IllegalArgumentException("You don't have enough money in your account!");
            }
            //ne folosim de makeTransactionOnCard din CardServiceImpl
            //Alegem cardul - trebuie sa aiba acelasi account cu cel calculat mai sus:
            var cards = cardService.getCards().stream().filter(c -> c.getAccount().getAccountId() == account.get().getAccountId()).toList();
            if(cards.isEmpty())
            {
                screen.displayMessage("You don't have any cards for this account!");
                return;
            }
            //Alegem cardul:
            screen.displayMessage("Choose the card you want to use:");
            cards.forEach(card -> screen.displayMessage(card.toString()));
            screen.displayMessage("Please enter the card's id:");
            var cardId = keyPad.readString();
            var card = cards.stream().filter(c -> Objects.equals(c.getCardId(), cardId)).findFirst();
            if(card.isEmpty())
            {
                screen.displayMessage("You don't have a card with this id!");
                return;
            }
            //Facem tranzactia:
            screen.displayMessage("Please enter a description for the transaction:");
            var description = keyPad.readLine();
            TransactionRepository.incrementTransactionNumber();
            Withdrawal withdrawal = new Withdrawal(String.valueOf(TransactionRepository.getTransactionNumber()),Double.parseDouble(amount), LocalDate.now(), description,account.get().getAccountId());
            cardService.makeTransactionOnCard(card.get().getCardId(), Double.parseDouble(amount), withdrawal);
            //Adaugam tranzactia in lista de tranzactii:
            transactionService.addTransaction(withdrawal);
            screen.displayMessage("Withdrawal made successfully!");
            auditService.addCommandToFile("Timestamp: " + LocalDateTime.now() + " - Withdrawal made from account " + account.get().getAccountId() + " with the amount of " + amount + " RON");
        } catch (IllegalArgumentException | AccountNotInListException | CardNotInListException e) {
            screen.displayMessage(e.getMessage());
        }
    }
    private void deposit(){
        screen.displayMessage("Choose the account to which you want to deposit:");
        Optional<Account> account = Optional.ofNullable(chooseAccount());
        if(account.isEmpty())
            return;
        try {
            screen.displayMessage("Enter the amount you want to deposit:");
            var amount = keyPad.readString();
            if(Double.parseDouble(amount) <= 0)
            {
                throw new IllegalArgumentException("The amount must be positive!");
            }
            //ne folosim de makeTransactionOnCard din CardServiceImpl
            //Alegem cardul - trebuie sa aiba acelasi account cu cel calculat mai sus:
            var cards = cardService.getCards().stream().filter(c -> c.getAccount().getAccountId() == account.get().getAccountId()).toList();
            if(cards.isEmpty())
            {
                screen.displayMessage("You don't have any cards for this account!");
                return;
            }
            //Alegem cardul:
            screen.displayMessage("Choose the card you want to use:");
            cards.forEach(card -> screen.displayMessage(card.toString()));
            screen.displayMessage("Please enter the card's id:");
            var cardId = keyPad.readString();
            var card = cards.stream().filter(c -> Objects.equals(c.getCardId(), cardId)).findFirst();
            if(card.isEmpty())
            {
                screen.displayMessage("You don't have a card with this id!");
                return;
            }
            //Facem tranzactia:
            screen.displayMessage("Please enter a description for the transaction:");
            var description = keyPad.readLine();
            TransactionRepository.incrementTransactionNumber();
            Deposit deposit = new Deposit(String.valueOf(TransactionRepository.getTransactionNumber()),Double.parseDouble(amount), LocalDate.now(), description, account.get().getAccountId());
            cardService.makeTransactionOnCard(card.get().getCardId(), Double.parseDouble(amount), deposit);
            //Adaugam tranzactia in lista de tranzactii:
            transactionService.addTransaction(deposit);
            screen.displayMessage("The deposit was successful!");
            auditService.addCommandToFile("Timestamp: " + LocalDateTime.now() + " - Deposit made on account " + account.get().getAccountId() + " with the amount of " + amount + " RON");
        }catch (IllegalArgumentException | AccountNotInListException | CardNotInListException e)
        {
            screen.displayMessage(e.getMessage());
        }
    }
    private Account chooseAccount()
    {
        int currentUserId = customerService.getCustomers().stream().filter(customer -> customer.getEntityId() == currentUser.getEntityId()).findFirst().get().getEntityId();
        var accounts = accountService.getAccounts().stream().filter(account -> account.getCustomerId() == currentUserId).toList();
        if(accounts.isEmpty())
        {
            screen.displayMessage("You don't have any accounts!");
            return null;
        }
        accounts.forEach(account -> screen.displayMessage(account.toString()));
        screen.displayMessage("Please enter the account's id:");
        var accountId = keyPad.readString();
        var account = accounts.stream().filter(acc -> acc.getAccountId() == Integer.parseInt(accountId)).findFirst();
        if(account.isEmpty())
        {
            screen.displayMessage("You don't have an account with this id!");
            return null;
        }
        return account.get();
    }
    private void viewBalance()
    {
        screen.displayMessage("Choose the account whose balance you want to view:");
        Optional<Account> account = Optional.ofNullable(chooseAccount());
        if(account.isEmpty())
            return;
        screen.displayMessage("Your balance is: " + account.get().getBalance());
        auditService.addCommandToFile("Timestamp: "+ LocalDateTime.now() + " View balance of user with id: " + currentUser.getEntityId() + " on account with id: " + account.get().getAccountId());

    }
    private boolean executeAdministratorAction(KeyPad.AdministratorAction chosenAction){
        switch(chosenAction)
        {
            case VIEW_ACCOUNTS -> {
                screen.displayMessage("Accounts:");
                accountService.getAccounts().forEach(account -> screen.displayMessage(account.toString()));
            }
            case VIEW_TRANSACTIONS -> {
                screen.displayMessage("Transactions:");
                transactionService.getTransactions().forEach(transaction -> screen.displayMessage(transaction.toString()));
            }
            case VIEW_CUSTOMERS -> {
                screen.displayMessage("Customers:");
                customerService.getCustomers().forEach(customer -> screen.displayMessage(customer.toString()));
            }
            case VIEW_CARDS -> {
                screen.displayMessage("Cards:");
                cardService.getCards().forEach(card -> screen.displayMessage(card.toString()));
            }
            case ADD_NEW_CUSTOMER -> {
                screen.displayMessage("Please introduce entity's id");
                var entityId = keyPad.readString();
                screen.displayMessage("Please introduce your desired id");
                var id = keyPad.readString();
                screen.displayMessage("Please enter the customer's name");
                var name = keyPad.readString();
                screen.displayMessage("Please enter the customer's surname");
                var surname = keyPad.readString();
                screen.displayMessage("Please enter the customer's email");
                var email = keyPad.readString();
                screen.displayMessage("Please enter the customer's cnp");
                var cnp = keyPad.readString();
                customerService.addCustomer(new Customer(Integer.parseInt(entityId),Integer.parseInt(id), name, surname, email, cnp));
            }

            case ADD_NEW_CARD_TO_SPECIFIC_CUSTOMER -> {
                screen.displayMessage("Please introduce the customer's id");
                var customerId = keyPad.readString();
                try{
                    Customer customer = customerService.getCustomerById(Integer.parseInt(customerId));
                    screen.displayMessage("What kind of card do you want to add?\n1. Credit\n2. Debit\n3. GiftCard\n4. PrepaidCard\n5. VirtualCard");
                    var chosenOption = keyPad.getOption();
                    switch (chosenOption){
                        case 1 -> addNewCardToCustomer(customer, "credit");
                        case 2 -> addNewCardToCustomer(customer, "debit");
                        case 3 -> addNewCardToCustomer(customer, "giftcard");
                        case 4 -> addNewCardToCustomer(customer, "prepaidcard");
                        case 5 -> addNewCardToCustomer(customer, "virtualcard");
                        default -> screen.displayMessage("Invalid option");
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
            case ADD_NEW_ACCOUNT_TO_SPECIFIC_CUSTOMER_CARD ->{
                screen.displayMessage("Please introduce the customer's id");
                var customerId = keyPad.readString();
                try{
                    Customer customer = customerService.getCustomerById(Integer.parseInt(customerId));
                    screen.displayMessage("Please introduce the card's id");
                    var cardId = keyPad.readString();
                    Card card = cardService.getCardById(cardId);
                    if(card.getCustomerId() != customer.getCustomerId()){
                        throw new CardNotInListException("The card is not in the list");
                    }
                    Account account = createNewAccount(customer);
                    if(account != null){
                        //accountService.addAccount(account);
                        cardService.addAccountToCard(card, account);
                        screen.displayMessage("Account added successfully");
                    }
                    else {
                        screen.displayMessage("Account not added, please try again");
                    }
                } catch (RuntimeException | CardNotInListException e) {
                    System.out.println(e.getMessage());
                }
            }
            case DELETE_ACCOUNT_OF_EXISTING_CUSTOMER -> {
                deleteAccountOfExistingCustomer();
            }
            case DELETE_CARD_OF_EXISTING_CUSTOMER -> {
                deleteCardOfExistingCustomer();
            }
            case DELETE_CUSTOMER -> {
                deleteCustomer();
            }
            case LOGOUT -> {
                currentUser = null;
                //Need to save the changes into the csv files:
                accountService.saveChanges();
                customerService.saveChanges();
                cardService.saveChanges();
                transactionService.saveChanges();
            }
            case EXIT -> {
                return false;
            }
        }
        return true;
    }

    private void deleteCustomer(){
        screen.displayMessage("Please introduce the customer's id");
        var customerId = keyPad.readString();
        try{
            Customer customer = customerService.getCustomerById(Integer.parseInt(customerId));
            customerService.deleteCustomer(customer);
            screen.displayMessage("Customer deleted successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private void deleteCardOfExistingCustomer(){
        screen.displayMessage("Please introduce the customer's id");
        var customerId = keyPad.readString();
        try{
            Customer customer = customerService.getCustomerById(Integer.parseInt(customerId));
            screen.displayMessage("Please introduce the card's id");
            var cardId = keyPad.readString();
            Card card = cardService.getCardById(cardId);
            if(card.getCustomerId() != customer.getCustomerId())
                throw new RuntimeException("The card is not owned by this customer");
            cardService.deleteCard(card);
            //also need to delete the card from the customer:
            customer.getCardList().remove(card);
            screen.displayMessage("Card deleted successfully");
        } catch (RuntimeException | CardNotInListException e) {
            System.out.println(e.getMessage());
        }
    }
    private void deleteAccountOfExistingCustomer(){
        screen.displayMessage("Please introduce the customer's id");
        var customerId = keyPad.readString();
        try{
            Customer customer = customerService.getCustomerById(Integer.parseInt(customerId));
            screen.displayMessage("Please introduce the account's id");
            var accountId = keyPad.readString();
            Account account = accountService.getAccountById(accountId);
            accountService.deleteAccount(account);
            //also need to delete the account from the card:
            for(Card c : customer.getCardList())
            {
                if(c.getAccount().equals(account))
                    c.setAccount(null);
            }
            screen.displayMessage("Account deleted successfully");
        } catch (RuntimeException | AccountNotInListException e) {
            System.out.println(e.getMessage());
        }
    }
    private Account createNewAccount(Customer customer) {
        screen.displayMessage("Please choose the type of account: \n1. CheckingAccount\n2. SavingsAccount");
        var chosenOption = keyPad.getOption();
        if (chosenOption == 1) {
                screen.displayMessage("Please introduce the account's id");
                var id = keyPad.readString();
                for(Account account : accountService.getAccounts()){
                    if(Objects.equals(account.getAccountId(), Integer.parseInt(id))){
                        screen.displayMessage("Account already exists");
                        return null;
                    }
                }
                screen.displayMessage("Please introduce the account's balance");
                var balance = keyPad.readString();
                screen.displayMessage("Please introduce the account's overdraft");
                var overdraft = keyPad.readString();
                CheckingAccount checkingAccount = new CheckingAccount(Integer.parseInt(id), customer.getCustomerId(), Double.parseDouble(balance), Double.parseDouble(overdraft));
                accountService.addAccount(checkingAccount);
                return checkingAccount;
            }
        else if (chosenOption == 2) {
                screen.displayMessage("Please introduce the account's id");
                var id = keyPad.readString();
                for(Account account : accountService.getAccounts()){
                    if(Objects.equals(account.getAccountId(), Integer.parseInt(id))){
                        screen.displayMessage("Account already exists");
                        return null;
                    }
                }
                screen.displayMessage("Please introduce the account's balance");
                var balance = keyPad.readString();
                screen.displayMessage("Please introduce the account's interest rate");
                var interestRate = keyPad.readString();
                SavingsAccount savingsAccount = new SavingsAccount(Integer.parseInt(id), customer.getCustomerId(), Double.parseDouble(balance), Double.parseDouble(interestRate));
                accountService.addAccount(savingsAccount);
                return savingsAccount;
            }
        else {
                screen.displayMessage("Invalid option");
                return null;
            }
        }
    private void addNewCardToCustomer(Customer customer, String type){
        try {
            screen.displayMessage("Please introduce the card's id");
            var id = keyPad.readString();
            //Checking if the card already exists
            for(Card card : cardService.getCards()){
                if(Objects.equals(card.getCardId(), id)){
                    throw new RuntimeException("Card already exists");
                }
            }

            screen.displayMessage("Please introduce your card number:");
            var cardNumber = keyPad.readString();
            screen.displayMessage("Please introduce your card's cvv:");
            var cvv = keyPad.readString();
            screen.displayMessage("Would you like to use an existing account or use a new one? \n1. Existing account\n2. New account");
            var chosenOption = keyPad.getOption();
            //If the customer wants to use an existing account:
            String accountId = null;
            Account account;
            if(chosenOption == 1)
            {
                screen.displayMessage("Please choose the account you want to link your card to:");
                if(accountService.getAccounts().stream().filter(acc -> acc.getCustomerId() == customer.getCustomerId()).toList().isEmpty())
                    {
                        screen.displayMessage("There are no accounts available");
                        return;
                    }
                accountService.getAccounts().stream().filter(acc -> acc.getCustomerId() == customer.getCustomerId()).forEach(acc -> screen.displayMessage(acc.toString()));
                accountId = keyPad.readString();
                account = accountService.getAccountById(accountId);
                if(account.getCustomerId() != customer.getCustomerId()){
                    throw new RuntimeException("Invalid account for this customer, please try again");
                }
            }
            else{
                account = createNewAccount(customer);
                if(account == null)
                    return;
            }

            //Adding other attributes depending on the card type:
            if(type.equals("credit"))
            {
                screen.displayMessage("Please introduce your card's limit:");
                var limit = keyPad.readString();
                System.out.println("Your balance is: " + account.getBalance());
                CreditCard creditCard = new CreditCard(id, customer.getCustomerId(), cardNumber, cvv, LocalDate.now().plusYears(7), account, Double.parseDouble(limit), account.getBalance());
                //cardService.addCard(creditCard);
                customerService.addCardToCustomer(customer, creditCard);
            }
            else if(type.equals("debit"))
            {
                System.out.println("Your balance is: " + account.getBalance());
                DebitCard debitCard = new DebitCard(id, customer.getCustomerId(), cardNumber, cvv, LocalDate.now().plusYears(7), account);
                //cardService.addCard(debitCard);
                customerService.addCardToCustomer(customer, debitCard);
            }
            else if(type.equals("giftcard"))
            {
                System.out.println("Please enter the giftcard's amount:");
                var amount = keyPad.readString();
                GiftCard giftCard = new GiftCard(id, customer.getCustomerId(), cardNumber, cvv, LocalDate.now().plusYears(7), account, Double.parseDouble(amount));
                //cardService.addCard(giftCard);
                customerService.addCardToCustomer(customer, giftCard);
            }
            else if(type.equals("prepaidcard"))
            {
                System.out.println("Please enter the prepaidcard's amount:");
                var amount = keyPad.readString();
                PrepaidCard prepaidCard = new PrepaidCard(id, customer.getCustomerId(), cardNumber, cvv, LocalDate.now().plusYears(7), account, Double.parseDouble(amount));
                //cardService.addCard(prepaidCard);
                customerService.addCardToCustomer(customer, prepaidCard);

            }
            else if(type.equals("virtualcard"))
            {
                System.out.println("Please enter the virtualcard's security code:");
                var securityCode = keyPad.readString();
                VirtualCard virtualCard = new VirtualCard(id, customer.getCustomerId(), cardNumber, cvv, LocalDate.now().plusYears(7), account, securityCode);
                //cardService.addCard(virtualCard);
                customerService.addCardToCustomer(customer, virtualCard);
            }
        } catch (RuntimeException | AccountNotInListException e){
            System.out.println(e.getMessage());
        }
    }


    private void login(){
        screen.displayMessage("Please login to continue");
        screen.displayMessage("Do you want to login as Administrator or Customer?\n1. Administrator\n2. Customer");
        var chosenOption = keyPad.getOption();

        if (chosenOption == 1) {
            loginAsAdministrator();
        } else if (chosenOption == 2) {
            loginAsCustomer();
        } else {
            screen.displayMessage("Invalid option");
        }
    }

    private void loginAsAdministrator(){
        screen.displayMessage("Please enter your username");
        var username = keyPad.readString();
        screen.displayMessage("Please enter your password");
        var password = keyPad.readString();

        if (username.equals("admin") && password.equals("admin")) {
            currentUser = new Administrator(0, username, password);
        } else {
            screen.displayMessage("Invalid username or password");
        }
    }

    private void loginAsCustomer() {
        screen.displayMessage("Please enter your cnp");
        var cnp = keyPad.readString();
        var customer = customerService.getCustomerByCnp(cnp);
        if(customer == null) {
            screen.displayMessage("Invalid cnp");
            return;
        }
        this.currentUser = customer;
        screen.displayMessage("Successfully logged in as " + customer.getLastName());
    }
}
