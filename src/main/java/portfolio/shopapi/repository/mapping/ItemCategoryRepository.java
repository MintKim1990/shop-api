package portfolio.shopapi.repository.mapping;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.mapping.ItemCategory;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
