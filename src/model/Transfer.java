package model;

import java.time.LocalDate;

public class Transfer extends Transaction{

    private Account toAccount;
    public Transfer(double amount, LocalDate date, String description, Account account, Account toAccount) {
        super(amount, date, description, account);
        this.toAccount = toAccount;
    }

    public Transfer(){

    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
}
