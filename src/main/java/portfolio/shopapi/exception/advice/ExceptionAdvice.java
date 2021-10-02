package portfolio.shopapi.exception.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import portfolio.shopapi.constant.ErrorType;
import portfolio.shopapi.exception.ParameterException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorInfo parameterExcetionHandler(ParameterException Error) {
        return new ErrorInfo(
                ErrorType.PARAMETER_ERROR,
                ErrorType.PARAMETER_ERROR.getMessage(),
                Optional.ofNullable(Error.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(error -> new ErrorDetailInfo(
                                error.getCode(),
                                error.getObjectName(),
                                error.getDefaultMessage()
                        ))
                        .collect(Collectors.toList()))
                        .orElseGet(null)
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorInfo exceptionHandler(Exception Error) {
        return new ErrorInfo(
                ErrorType.ERROR,
                Error.getMessage(),
                null
        );
    }

    @Data
    @AllArgsConstructor
    static class ErrorInfo {

        private ErrorType errorType;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<ErrorDetailInfo> detail;
    }

    @Data
    @AllArgsConstructor
    static class ErrorDetailInfo {
        private String code;
        private String objectName;
        private String message;
    }

}
