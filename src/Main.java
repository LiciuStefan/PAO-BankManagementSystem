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
import service.impl.AccountServiceImpl;
import service.impl.CardServiceImpl;
import service.impl.CustomerServiceImpl;
import service.impl.TransactionServiceImpl;
import util.KeyPad;
import util.Screen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static constants.Constants.*;

public class Main {
    public static void main(String[] args) throws CardNotInListException, EmptyListException {

        CustomerServiceImpl customerService = new CustomerServiceImpl();
        AccountServiceImpl accountService = new AccountServiceImpl();
        CardServiceImpl cardService = new CardServiceImpl();
        TransactionServiceImpl transactionService = new TransactionServiceImpl();

        Screen screen = new Screen();
        App app = new App(new KeyPad(screen), screen, accountService, cardService, customerService, transactionService);
        app.run();
    }
}