package portfolio.shopapi.request.order;

import lombok.Builder;
import lombok.Data;

@Data
public class Items {

    private Long itemId;
    private int itemCount;

    @Builder
    public Items(Long itemId, int itemCount) {
        this.itemId = itemId;
        this.itemCount = itemCount;
    }
}
