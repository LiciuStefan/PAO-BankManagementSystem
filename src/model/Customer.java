package model;

import constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO'S: add account and delete accont methods - keep accounts ordered?
public class Customer extends BankEntity{
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String cnp;

    private List<Card> cardList;

    public Customer(int entityId, int customerId, String firstName, String lastName, String email, String cnp) {
        super(entityId, Constants.CUSTOMER);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cnp = cnp;
        this.cardList = new ArrayList<>();
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

    public List<Card> getCardList() {
        return cardList;
    }

    public void setAccountList(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email) && Objects.equals(cnp, customer.cnp) && Objects.equals(cardList, customer.cardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId, firstName, lastName, email, cnp, cardList);
    }
}
