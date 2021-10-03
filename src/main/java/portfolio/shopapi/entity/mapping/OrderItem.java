package portfolio.shopapi.entity.mapping;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.order.Order;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int itemCount;
    private int totalPrice;

    @Builder
    private OrderItem(Long id, Item item, Order order, int orderPrice, int itemCount, int totalPrice) {
        this.id = id;
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.itemCount = itemCount;
        this.totalPrice = totalPrice;
    }

    /**
     * 주문 상세내역 생성
     * @param item
     * @param orderPrice
     * @param itemCount
     * @return
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int itemCount) {

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .itemCount(itemCount)
                .totalPrice(orderPrice * itemCount)
                .build();

        item.removeStock(itemCount); // 변경감지로 인한 DB 처리
        return orderItem;
    }

    /**
     * 주문 취소시 상품 총수 증가
     */
    public void orderCancel() {
        getItem().addStock(itemCount); // 변경감지로 인한 DB 처리
    }

    /**
     * 주문 가격 조회
     * @return
     */
    public int orderPrice() {
        return getTotalPrice();
    }



}
