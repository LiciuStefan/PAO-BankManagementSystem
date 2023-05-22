package model;

import java.time.LocalDate;

public class Withdrawal extends Transaction{

    public Withdrawal(double amount, LocalDate date, String description,int accountId) {
        super(amount, date, description, accountId);
    }

    public Withdrawal(String transactionId, double amount, LocalDate date, String description, int accountId) {
        super(transactionId, amount, date, description, accountId);
    }
    public Withdrawal(){

    }

    @Override
    public String toCSV(){
        return "Withdrawal," + super.toCSV();
    }
}
