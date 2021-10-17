package portfolio.shopapi.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
