package portfolio.shopapi.response.order;

import lombok.Data;

@Data
public class OrderItemResponse {

    private String name;
    private int price;
    private int stockQuantity;
    private String categoryCode;

    public OrderItemResponse(String name, int price, int stockQuantity, String categoryCode) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", categoryCode='" + categoryCode + '\'' +
                '}';
    }
}
