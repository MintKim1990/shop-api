package portfolio.shopapi.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorType {

    ERROR(1, "오류"),
    PARAMETER_ERROR(2, "파라미터 입력값이 비정상");

    @Getter
    private final int errorCode;

    @Getter
    private final String message;


}
