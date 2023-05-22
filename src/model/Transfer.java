package model;

import java.time.LocalDate;

public class Transfer extends Transaction{

    private int otherAccountId;
    public Transfer(double amount, LocalDate date, String description, int accountId, int otherAccountId) {
        super(amount, date, description, accountId);
        this.otherAccountId = otherAccountId;
    }

    public Transfer(String transactionId, double amount, LocalDate date, String description, int accountId, int otherAccountId) {
        super(transactionId, amount, date, description, accountId);
        this.otherAccountId = otherAccountId;
    }

    public Transfer(){

    }

    public int getOtherAccountId() {
        return otherAccountId;
    }

    public void setOtherAccountId(int otherAccountId) {
        this.otherAccountId = otherAccountId;
    }

    @Override
    public String toCSV(){
        return "Transfer," + super.toCSV() + "," + otherAccountId;
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1) +
                ", otherAccountId=" + otherAccountId +
                '}';
    }
}
