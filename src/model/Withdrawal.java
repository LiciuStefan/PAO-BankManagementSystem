package model;

import java.time.LocalDate;

public class Withdrawal extends Transaction{

    public Withdrawal(double amount, LocalDate date, String description, Account account) {
        super(amount, date, description, account);
    }

    public Withdrawal(){

    }
}
