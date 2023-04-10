package model;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private int bankId;
    private String bankName;

    //List of all bank entities (customers, employees, manager)
    private List<BankEntity> bankEntities;

    public Bank(int bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankEntities = new ArrayList<>();
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<BankEntity> getBankEntities() {
        return bankEntities;
    }

    public void setBankEntities(List<BankEntity> bankEntities) {
        this.bankEntities = bankEntities;
    }
}
