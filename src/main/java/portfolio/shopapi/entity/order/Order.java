package portfolio.shopapi.entity.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.constant.OrderStatus;
import portfolio.shopapi.entity.embedded.Address;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.mapping.OrderItem;
import portfolio.shopapi.entity.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Embedded
    private Delivery delivery;

    private LocalDateTime orderDateTime;

    /*
        Enumerated Default 타입 : EnumType.ORDINAL
        ORDINAL 일경우 DB에 숫자타입으로 저장되기때문에 나중에 enum 이 변경될경우 숫자가 변경되어 에러발생
        STRING 일경우에 DB에 문자타입으로 저장되기때문에 나중에 enum 이 변경되어도 안전
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 양방향 주입
    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    // 양방향주입
    private void setOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    private void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    private void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {

        Order order = new Order();

        order.setMember(member);
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDateTime(LocalDateTime.now());

        for (OrderItem orderItem : orderItems) {
            order.setOrderItems(orderItem);
        }

        return order;

    }

}
