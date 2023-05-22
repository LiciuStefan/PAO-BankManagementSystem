package util;

import java.util.Scanner;

public class KeyPad {

    private Scanner scanner;
    private Screen screen;

    public KeyPad(Screen screen) {
        this.scanner = new Scanner(System.in);
        this.screen = screen;
    }

    public Action getAction() {
        return Action.getActionByNumber(scanner.nextInt());
    }

    public AdministratorAction getAdministratorAction() {
        return AdministratorAction.getActionByNumber(scanner.nextInt());
    }

    public enum Action {
        VIEW(1),
        DEPOSIT(2),
        WITHDRAWAL(3),
        TRANSFER(4),
        PAYMENT(5),
        LOGOUT(6),
        EXIT(7),
        NONE(0);

        private final int keyNumber;

        Action (int keyNumber) {
            this.keyNumber = keyNumber;
        }

        private static Action getActionByNumber(int keyNumber) {
            return java.util.Arrays.stream(Action.values())
                    .filter(action -> action.keyNumber == keyNumber)
                    .findFirst()
                    .orElse(NONE);
        }
    }

    public enum AdministratorAction {
        VIEW_ACCOUNTS(1),
        VIEW_TRANSACTIONS(2),
        VIEW_CUSTOMERS(3),
        VIEW_CARDS(4),
        ADD_NEW_CUSTOMER(5),
        ADD_NEW_CARD_TO_SPECIFIC_CUSTOMER(6),
        ADD_NEW_ACCOUNT_TO_SPECIFIC_CUSTOMER_CARD(7),
        DELETE_ACCOUNT_OF_EXISTING_CUSTOMER(8),
        DELETE_CARD_OF_EXISTING_CUSTOMER(9),
        DELETE_CUSTOMER(10),
        LOGOUT(11),
        EXIT(12),
        NONE(0);

        private final int keyNumber;

        AdministratorAction (int keyNumber) {
            this.keyNumber = keyNumber;
        }

        private static AdministratorAction getActionByNumber(int keyNumber) {
            return java.util.Arrays.stream(AdministratorAction.values())
                    .filter(action -> action.keyNumber == keyNumber)
                    .findFirst()
                    .orElse(NONE);
        }
    }

    public String readLine(){
        scanner.nextLine();
        return scanner.nextLine();
    }

    public int getOption() {
        var input = -1;

        while (input == -1) {
            try {
                String inputString = scanner.next();
                input = Integer.parseInt(inputString);
            } catch (NumberFormatException e) {
                screen.displayMessage("Invalid input format. Try again!");
            }
        }

        return input;
    }

    public double getAmount() {
        var input = -1.0;

        while (input == -1) {
            try {
                String inputString = scanner.next();
                input = Double.parseDouble(inputString);
            } catch (NumberFormatException e) {
                screen.displayMessage("Invalid input format. Try again!");
            }
        }
        return input;
    }

    public String readString(){
        return scanner.next();
    }

}
