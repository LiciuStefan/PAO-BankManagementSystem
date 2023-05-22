package util;

public class Screen {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayMenu() {
        displayMessage("""
                -------------------
                1. View balance
                2. Deposit
                3. Withdraw
                4. Make a transfer
                5. Make a payment
                6. Logout
                7. Exit
                """);
    }

    public void displayAdministratorMenu(){
        displayMessage("""
                -------------------
                1. View all accounts
                2. View all transactions
                3. View all customers
                4. View all cards
                5. Add new customer
                6. Add new card to specific customer
                7. Add new account to specific customer's card
                8. Delete account of existing customer
                9. Delete card of existing customer
                10. Delete customer
                11. Logout
                12. Exit
                """);
    }
}
