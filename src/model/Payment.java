package model;

import java.time.LocalDate;

public class Payment extends Transaction{

    public Payment(double amount, LocalDate date, String description, Account account) {
        super(amount, date, description, account);
    }

    public Payment(){

    }
}
