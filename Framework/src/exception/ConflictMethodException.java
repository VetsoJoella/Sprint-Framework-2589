package exception;

public class ConflictMethodException extends Exception {
    
    public ConflictMethodException() {
        super();
    }

    // Constructeur avec un message d'erreur personnalisé
    public ConflictMethodException(String message) {
        super(message);
    }

    // Constructeur avec message d'erreur et cause
    public ConflictMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec cause
    public ConflictMethodException(Throwable cause) {
        super(cause);
    }
}
