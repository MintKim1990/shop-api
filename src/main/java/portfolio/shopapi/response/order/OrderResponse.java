package portfolio.shopapi.response.order;

import lombok.Data;
import portfolio.shopapi.entity.constant.OrderStatus;
import portfolio.shopapi.entity.embedded.Address;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.mapping.OrderItem;
import portfolio.shopapi.entity.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {

    private String membername;
    private Address memberaddress;
    private String memberphone;

    private List<OrderItemResponse> items;

    private int totalPrice;

    private Delivery delivery;

    private LocalDateTime orderDateTime;
    private OrderStatus status;

    public OrderResponse(Order order) {

        this.membername = order.getMember().getName();
        this.memberaddress = order.getMember().getAddress();
        this.memberphone = order.getMember().getPhone();

        this.items = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemResponse(orderItem))
                .collect(Collectors.toList());

        this.totalPrice = order.getOrderItems().stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();

        this.delivery = order.getDelivery();
        this.orderDateTime = order.getOrderDateTime();
        this.status = order.getStatus();

    }

}
