package portfolio.shopapi.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.Category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
