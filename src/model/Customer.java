package model;

import constants.Constants;

import java.util.ArrayList;
import java.util.List;

//TODO'S: add account and delete accont methods - keep accounts ordered?
public class Customer extends BankEntity{
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String cnp;

    private List<Account> accountList;

    public Customer(int entityId, int customerId, String firstName, String lastName, String email, String cnp) {
        super(entityId, Constants.CUSTOMER);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cnp = cnp;
        this.accountList = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
