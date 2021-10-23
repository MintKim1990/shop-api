package portfolio.shopapi.repository.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.order.Order;

import java.util.List;

import static portfolio.shopapi.entity.item.QItem.item;
import static portfolio.shopapi.entity.mapping.QOrderItem.orderItem;
import static portfolio.shopapi.entity.member.QMember.member;
import static portfolio.shopapi.entity.order.QOrder.order;

;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findOrders(Long memberId) {
        return queryFactory.select(order)
                .distinct()
                .from(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(order.member.id.eq(memberId))
                .fetch();

    }

}
