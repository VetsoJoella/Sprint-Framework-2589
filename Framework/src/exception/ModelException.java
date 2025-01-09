package exception;

public class ModelException extends Exception{

    Object object ;

    public ModelException(String message){
        super(message);
    }

    public ModelException(){}

      // Constructeur avec message d'erreur et cause
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec cause
    public ModelException(Throwable cause) {
        super(cause);
    }
    
}
