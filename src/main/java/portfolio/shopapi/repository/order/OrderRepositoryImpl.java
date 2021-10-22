package portfolio.shopapi.repository.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.response.order.OrderItemResponse;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.List;

import static portfolio.shopapi.entity.category.QCategory.category;
import static portfolio.shopapi.entity.item.QItem.item;
import static portfolio.shopapi.entity.mapping.QItemCategory.itemCategory;
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

//        return queryFactory.select(
//                        // CategoryListResponse 객체에 생성자로 데이터 삽입
//                        Projections.constructor(
//                                OrderResponse.class,
//                                member.name,
//                                member.address,
//                                member.phone,
//                                Projections.list(
//                                        Projections.constructor(
//                                                OrderItemResponse.class,
//                                                item.name,
//                                                item.price,
//                                                item.stockQuantity,
//                                                category.code
//                                        )
//                                ),
//                                orderItem.orderPrice,
//                                orderItem.itemCount,
//                                orderItem.totalPrice,
//                                order.delivery,
//                                order.orderDateTime,
//                                order.status
//                        )
//                )
//                .distinct()
//                .from(order)
//                .join(order.member, member)
//                .join(order.orderItems, orderItem)
//                .join(orderItem.item, item)
//                .leftJoin(item.itemCategories, itemCategory)
//                .leftJoin(itemCategory.category, category)
//                .where(order.member.id.eq(memberId))
//                .fetch();

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
