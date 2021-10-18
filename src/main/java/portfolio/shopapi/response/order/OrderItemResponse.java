package portfolio.shopapi.response.order;

import lombok.Data;

@Data
public class OrderItemResponse {

    private String name;
    private int price;
    private int stockQuantity;

    public OrderItemResponse(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
