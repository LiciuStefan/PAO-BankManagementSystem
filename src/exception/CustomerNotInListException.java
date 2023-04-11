package exception;

public class CustomerNotInListException extends RuntimeException{
    public CustomerNotInListException(String message) {
        super(message);
    }
}
