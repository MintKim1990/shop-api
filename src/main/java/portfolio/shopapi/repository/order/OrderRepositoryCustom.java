package portfolio.shopapi.repository.order;

import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findOrders(Long memberId);
}
