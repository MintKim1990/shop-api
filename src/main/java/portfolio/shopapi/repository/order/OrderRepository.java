package portfolio.shopapi.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query("select distinct o from Order o" +
            " join fetch o.member m" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i" +
            " where m.id = :memberId")
    List<Order> findOrdersByMemberId(@Param("memberId") Long memberId);

    @Query("select c from Category c" +
            " join fetch c.itemCategories ic" +
            " where ic.item.id = :itemId")
    List<Category> findOrderCategorysByItemId(@Param("itemId") Long itemId);

}
