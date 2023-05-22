package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bank {
    private int bankId;
    private String bankName;

    //List of all bank entities (customers)
    private HashMap<Integer, BankEntity> bankEntities;

    public Bank(int bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankEntities = new HashMap<>();
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

    public HashMap<Integer, BankEntity> getBankEntities() {
        return bankEntities;
    }

    public void setBankEntities(HashMap<Integer, BankEntity> bankEntities) {
        this.bankEntities = bankEntities;
    }
}
