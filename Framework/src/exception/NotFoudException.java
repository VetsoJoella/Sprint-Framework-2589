package exception;

public class NotFoudException extends Exception {
    
    public NotFoudException() {
        super();
    }

    // Constructeur avec un message d'erreur personnalisé
    public NotFoudException(String message) {
        super(message);
    }

    // Constructeur avec message d'erreur et cause
    public NotFoudException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec cause
    public NotFoudException(Throwable cause) {
        super(cause);
    }
}
