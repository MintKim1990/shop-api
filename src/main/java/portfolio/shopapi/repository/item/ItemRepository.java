package portfolio.shopapi.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.item.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
