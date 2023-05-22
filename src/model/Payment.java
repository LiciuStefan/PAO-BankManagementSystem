package model;

import java.time.LocalDate;

public class Payment extends Transaction{

    public Payment(double amount, LocalDate date, String description, int accountId) {
        super(amount, date, description, accountId);
    }

    public Payment(String transactionId, double amount, LocalDate date, String description, int accountId) {
        super(transactionId, amount, date, description, accountId);
    }

    public Payment(){

    }

    @Override
    public String toCSV(){
        return "Payment," + super.toCSV();
    }
}
