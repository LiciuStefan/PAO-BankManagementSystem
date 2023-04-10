package model;

import java.time.LocalDate;

//TODO'S: find some things to add
public class Deposit extends Transaction{

    public Deposit(double amount, LocalDate date, String description, Account account) {
        super(amount, date, description, account);
    }

    public Deposit(){

    }
}
