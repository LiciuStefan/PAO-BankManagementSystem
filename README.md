# Proiect PAO - Liciu Stefan - grupa 234 🤳 #
**Aplicatie Java ce gestioneaza backend-ul unui sistem bancar 💸**  
Aceasta ofera o serie de functionalitati pentru gestiunea tranzactiilor financiare si gestiunea datelor clientilor.  
Aceste servicii includ operatiuni precum: depunere, retragere, transfer intre conturi, deschidere cont cu/fara card afiliat, interogare sold.  
 
## 🏦 Entitati prezente in aplicatie: ##
*click on the arrow to extend*
<details>
  <summary> Bank </summary>
  <p>bankId</p>
  <p>bankName</p>
  <p>bankEntities (HashMap) </p>
</details>

<details>
  <summary> BankEntity (abstract)</summary>
  <p>entityId</p>
  <p>entityName</p>
</details>

<details>
  <summary>Customer (extends BankEntity)</summary>
  <p>customerId</p>
  <p>firstName</p>
  <p>lastName</p>
  <p>email</>
  <p>cnp</p>
</details>

<details>
  <summary>Account (abstract)</summary>
  <p>accountId</p>
  <p>balance</p>
  <p>transactionList (List of type Transaction)</p>
</details>

<details>
  <summary>CheckingAccount (extends Account)</summary>
  <p>overdraftLimit</p>
</details>

<details>
  <summary>SavingsAccount (extends Account)</summary>
  <p>interestRate</p>
</details>

<details>
  <summary>Card (abstract)</summary>
  <p>cardId</p>
  <p>cardNumber</p>
  <p>cvv</p>
  <p>expirationDate</p>
  <p>account (Object of type account that represents the current account on which the card is affiliated</p>
</details>

<details>
  <summary>CreditCard (extends Card)</summary>
  <p>creditLimit</p>
  <p>balance</p>
</details>

<details>
  <summary>DebitCard (extends Card)</summary>
 </details>

<details>
  <summary>GiftCard (extends Card)</summary>
  <p>amount</p>
</details>

<details>
  <summary>PrepaidCard (extends Card) </summary>
  <p>balance</p>
</details>

<details>
  <summary>VirtualCard (extends Card) </summary>
  <p>securityCode</p>
</details>

<details>
  <summary>Transaction (abstract) </summary>
  <p>transactionIdCounter/p>
  <p>transactionId</p>
  <p>amount</p>
  <p>date</p>
  <p>description</p>
  <p>account</p>
</details>

<details>
  <summary>Deposit (extends Transaction)</summary>
</details>

<details>
  <summary>Withdrawal (extends Transaction)</summary>
</details>

<details>
  <summary>Payment (extends Transaction)</summary>
</details>

<details>
  <summary>Transfer (extends Transaction)</summary>
  <p>toAccount (to which account do we transfer) </p>
</details>
   
***
## 🚨 Services si actiuni/interogari pentru fiecare: 🚨 ##
1. AccountService 📠 
* addAccount - adauga cont in lista de conturi  
```java
        AccountServiceImpl accountService = new AccountServiceImpl();
        CheckingAccount account1 = new CheckingAccount(1, 7000, 5000);
        accountService.addAccount(account1);

```  
* getAccounts - lista cu toate conturile existente
```java
        List<Account> accounts = accountService.getAccounts();
```
* getAccountById - obtinerea unui cont cu id-ul specificat - throws AccountNotInListException
```java
        try {
            Account account = accountService.getAccountById("1");
            System.out.println("Account 1: ");
            System.out.println(account);
        } catch (AccountNotInListException e) {
            System.out.println(e.getMessage());
        }
```
* deleteAccount - eliminare cont din lista de conturi throws AccountNotInListException
```java
        try {
            accountService.deleteAccount(account1);
            System.out.println("Deleted account 1");
        } catch (AccountNotInListException e) {
            System.out.println(e.getMessage());
        }
```
* getAllAccountsThatHaveSpecificTransactionType - returneaza toate conturile care au un anumit tip de tranzactie (Deposit, Withdrawal, Payment, Transfer)
```java
List<Account> accounts = accountService.getAllAccountsThatHaveSpecificTransactionType("Deposit");
```
* addTransactionToAccount - adauga tranzactie - throws AccountNotInListException
```java
accountService.addTransactionToAccount(account1, new Deposit(1000, LocalDate.now(), "Deposit", account1));
```
2. CardService 💳
* addCard - adauga card in lista
```java
        CardServiceImpl cardService = new CardServiceImpl();
        Card card1 = new CreditCard("1", "1000", "153", LocalDate.of(2021, 12, 31), account1, account1.getOverdraftLimit(), account1.getBalance());
        Card card2 = new DebitCard("2", "2000", "254", LocalDate.of(2021, 12, 31), account2);
        Card card3 = new GiftCard("3", "3000", "355", LocalDate.now(), account3, 1000);
        Card card4 = new PrepaidCard("4", "4000", "456", LocalDate.of(2024, 1, 15), account4, 1000);
        Card card5 = new VirtualCard("5", "5000", "557", LocalDate.of(2025, 12, 16), account1, "1234567890");

        cardService.addCard(card1);

```
* getCards - returneaza lista cardurilor active
```java
  List<Card> cardList = cardService.getCards();
```
* getCardById - returneaza card dupa id
```java
  Card card = cardService.getCardById("1");
```
* getAllCardsThatExpireBeforeSpecificDate - returneaza lista cardurilor care expira inaintea unei anumite date - throws EmptyListException
```java
        try{
            List<Card> cardsThatExpireBeforeSpecificDate = cardService.getAllCardsThatExpireBeforeSpecificDate("2023-12-31");

            System.out.println("Cards that expire before 2023-12-31: ");
            for (Card c : cardsThatExpireBeforeSpecificDate) {
                System.out.println(c);
            }
        }catch (EmptyListException e){
            System.out.println(e.getMessage());
        }

```
* getAllCardsThatBelongToSpeficAccount - intoarce toate cardurile care sunt afiliate unui anumit cont - throws EmptyListException
```java
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
```
* makeTransactionOnCard - face o tranzactie asupra unui anumit card - tranzactia poate fi de orice tip, foloseste metodele  
definite in acelasi Service makePayment(), makeDeposit(), makeWithdrawal(), makeTransfer()  
insereaza cu binarySearch() pt a tine lista sortata
- trows CardNotInListException
```java
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
//All transactions made for account 1:
        List<Transaction> transactions = account1.getTransactionList();

        for (Transaction t : transactions) {
            System.out.println(t.getDescription());
        }
```
3. CustomerService 👨‍💼 - foloseste cardService
* addCard - adauga Card in lista pentru un anumit Customer
```java
        Card cardCustomer = new CreditCard("5", "1111", "178", LocalDate.of(2021, 12, 31), account1, account1.getOverdraftLimit(), account1.getBalance());

        //Adding card to customer:
        customerService.addCard(customer1, cardCustomer);
```
* addCustomer - adauga Customer in lista
```java
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        Customer customer1 = new Customer(1, 1, "Stefan", "Liciu", "stefanliciu@gmail.com", "123456789");
        Customer customer2 = new Customer(2, 2, "Eduard", "Sabau", "sabaueduard@gmail.com", "987654321");

        customerService.addCustomer(customer1);
        customerService.addCustomer(customer2);
```
* getCustomers - returneaza lista clientilor
```java
List<Customer> customerList = customerService.getCustomers();
```
* getCustomerByFullName - returneaza un customer specific dupa nume intreg
```java
Customer customer = customerService.getCustomerByFullName("Stefan", "Liciu");
```
* getCustomerThatHasSpecificCard - returneaza un client ce are un anumit card
```java
        try
        {
           Customer c = customerService.getCustomerThatHasSpecificCard(cardCustomer.getCardId());
            System.out.println(c.getFirstName());
        }catch (CustomerNotInListException e){
            System.out.println(e.getMessage());
        }
```
* makeTransactionOnCustomerUsingCard - face tranzactii clientului folosit un anumit card - throws CustomerNotInListException  
insereaza folosind binarySearch pentru a tine lista sortata
```java
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
```  
***
## Exceptions 🚫 ##
* AccountNotInListException - contul nu este in lista
* BankEntityNotFounException - entitatea nu este in lista
* CardNotInListException - cardul nu este in lista
* CustomerNotInListException - clientul nu este in lista
* EmptyListException - lista goala
* InsuficientFundsException - fonduri insuficiente pentru tranzactie
* InvalidAmountException - suma incorecta pentru tranzactie (eg < 0)  
***
## Constants 📍 ##
```java
    public static final String CUSTOMER = "Customer";
    public static final String EMPLOYEE = "Employee"; //to be implemented
    public static final String MANAGER = "Manager"; //to be implemented

```
***
## Utils 🛠 ##
Compare amounts - compara doua sume
```java
 public static void validateAmount(double amount1, double amount2)
    {
        if(amount1 > amount2)
            throw new InsuficientFundsException("Insuficient funds");
    }
```  
***
## To be continued... 🔜 ##
* clase manager + employee (extends BankEntity)
* validation (transaction, account, card)
* bankService
* repositories
* scos metode din clasele de tip card (makeTransfer...)
