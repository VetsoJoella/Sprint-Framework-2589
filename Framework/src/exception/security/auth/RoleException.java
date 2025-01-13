package exception.security.auth;

public class RoleException extends Exception{
    
    public RoleException() {
        super();
    }

    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
