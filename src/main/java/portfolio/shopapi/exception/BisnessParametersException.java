package portfolio.shopapi.exception;

public class BisnessParametersException extends RuntimeException{

    public BisnessParametersException() {
        super();
    }

    public BisnessParametersException(String message) {
        super(message);
    }

    public BisnessParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public BisnessParametersException(Throwable cause) {
        super(cause);
    }

    protected BisnessParametersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
