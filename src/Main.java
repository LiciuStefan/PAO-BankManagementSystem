import config.DatabaseConfiguration;
import exception.AccountNotInListException;
import exception.CardNotInListException;
import exception.CustomerNotInListException;
import exception.EmptyListException;
import model.*;
import repository.AccountRepository;
import repository.CardRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.CardService;
import service.CustomerService;
import service.impl.*;
import util.KeyPad;
import util.Screen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static constants.Constants.*;

public class Main {
    public static void main(String[] args) throws CardNotInListException, EmptyListException, SQLException {

        /*CustomerServiceImpl customerService = new CustomerServiceImpl();
        AccountServiceImpl accountService = new AccountServiceImpl();
        CardServiceImpl cardService = new CardServiceImpl();
        TransactionServiceImpl transactionService = new TransactionServiceImpl();

        Screen screen = new Screen();
        App app = new App(new KeyPad(screen), screen, accountService, cardService, customerService, transactionService);
        app.run();
        */

        CustomerServiceDatabase customerService = new CustomerServiceDatabase();
        AccountServiceDatabase accountService = new AccountServiceDatabase();
        CardServiceDatabase cardService = new CardServiceDatabase();
        TransactionServiceDatabase transactionService = new TransactionServiceDatabase();

        Screen screen = new Screen();
        AppRunFromDatabase app = new AppRunFromDatabase(new KeyPad(screen), screen, accountService, cardService, customerService, transactionService);
        app.run();
    }
}