package exception.security.auth;

public class AuthentificationException extends Exception {
    

    public AuthentificationException() {
        super();
    }

    public AuthentificationException(String message) {
        super(message);
    }

    public AuthentificationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
