package portfolio.shopapi.response.order;

import lombok.Data;
import portfolio.shopapi.entity.constant.OrderStatus;
import portfolio.shopapi.entity.embedded.Address;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private String membername;
    private Address memberaddress;
    private String memberphone;

    private List<Item> items;

    private int orderPrice;
    private int itemCount;
    private int totalPrice;

    private Delivery delivery;

    private LocalDateTime orderDateTime;
    private OrderStatus status;

    public OrderResponse(String membername, Address memberaddress, String memberphone, List<Item> items, int orderPrice, int itemCount, int totalPrice, Delivery delivery, LocalDateTime orderDateTime, OrderStatus status) {
        this.membername = membername;
        this.memberaddress = memberaddress;
        this.memberphone = memberphone;
        this.items = items;
        this.orderPrice = orderPrice;
        this.itemCount = itemCount;
        this.totalPrice = totalPrice;
        this.delivery = delivery;
        this.orderDateTime = orderDateTime;
        this.status = status;
    }
}
