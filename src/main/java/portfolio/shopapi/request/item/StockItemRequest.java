package portfolio.shopapi.request.item;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StockItemRequest {

    @NotNull(message = "상품아이디는 필수입니다.")
    private Long id;

    @NotNull(message = "상품수량은 필수입니다.")
    private Integer stockQuantity;

    @Builder
    public StockItemRequest(Long id, Integer stockQuantity) {
        this.id = id;
        this.stockQuantity = stockQuantity;
    }
}
