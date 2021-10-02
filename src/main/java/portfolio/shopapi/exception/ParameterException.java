package portfolio.shopapi.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

public class ParameterException extends RuntimeException {

    @Getter
    private BindingResult bindingResult;

    public ParameterException() {
        super();
    }

    public ParameterException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }

    protected ParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
