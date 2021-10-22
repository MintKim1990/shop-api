package portfolio.shopapi.response.order;

import lombok.Data;
import portfolio.shopapi.entity.mapping.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderItemResponse {

    private String name;
    private int price;
    private int stockQuantity;
    private List<String> categoryCode = new ArrayList<>();
    private int totalPrice;
    private int itemCount;


    public OrderItemResponse(OrderItem orderItem) {

        this.name = orderItem.getItem().getName();
        this.price = orderItem.getItem().getPrice();
        this.stockQuantity = orderItem.getItem().getStockQuantity();
        this.totalPrice = orderItem.getTotalPrice();
        this.itemCount = orderItem.getItemCount();

        // 상품카테고리 지연로딩 실행 (default_batch_fetch_size : 1000)
        orderItem.getItem().getItemCategories()
                .stream()
                .forEach(itemCategory -> {
                    this.categoryCode.add(
                            itemCategory.getCategory().getName()
                    ); // 카테고리 지연로딩 실행 (default_batch_fetch_size : 1000)
                });
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", categoryCode=" + categoryCode +
                '}';
    }
}
