package model;

public class Administrator extends BankEntity{

    private String password;
    public Administrator(int entityId, String entityName, String password) {
        super(entityId, entityName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
