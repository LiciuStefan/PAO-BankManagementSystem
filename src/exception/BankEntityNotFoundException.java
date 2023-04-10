package exception;

public class BankEntityNotFoundException extends RuntimeException{

    public BankEntityNotFoundException(String message) {
        super(message);
    }
}
