package model;

import java.time.LocalDate;

//TODO'S: find some things to add
public class Deposit extends Transaction{

    public Deposit(double amount, LocalDate date, String description, int accountId) {
        super(amount, date, description, accountId);
    }

    public Deposit(String transactionId, double amount, LocalDate date, String description, int accountId) {
        super(transactionId, amount, date, description, accountId);
    }

    public Deposit(){

    }

    @Override
    public String toCSV(){
        return "Deposit," + super.toCSV();
    }
}
