package portfolio.shopapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import portfolio.shopapi.constant.ResponseType;

@Data
@AllArgsConstructor
public class Response<T> {
    private ResponseType Status;
    private T data;
}
