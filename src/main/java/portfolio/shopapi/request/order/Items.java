package portfolio.shopapi.request.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Items {

    @NotNull(message = "상품ID는 필수입니다.")
    private Long itemId;
    @NotNull(message = "상품구매 건수는 필수입니다.")
    private Integer itemCount;

    @Builder
    public Items(Long itemId, Integer itemCount) {
        this.itemId = itemId;
        this.itemCount = itemCount;
    }
}
