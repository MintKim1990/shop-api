package portfolio.shopapi.request.order;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderItemRequest {
    @NotEmpty(message = "주문상품 정보는 필수입니다.")
    List<Items> itemsList;
}
