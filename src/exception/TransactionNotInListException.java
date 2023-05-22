package exception;

public class TransactionNotInListException extends RuntimeException{

    public TransactionNotInListException(String message) {
        super(message);
    }
}
