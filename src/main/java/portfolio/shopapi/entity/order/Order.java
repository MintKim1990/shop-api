package portfolio.shopapi.entity.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.constant.OrderStatus;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.mapping.OrderItem;
import portfolio.shopapi.entity.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "order")
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

}
