package pl.polsl.friendnest.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
        super("Błąd, skontaktuj się z administratorem");
    }
}
