package exception;

public class AccountNotInListException extends Exception{
    public AccountNotInListException(String message) {
        super(message);
    }
}
