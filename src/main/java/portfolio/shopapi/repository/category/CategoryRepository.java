package portfolio.shopapi.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portfolio.shopapi.entity.category.Category;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Category c where c.code = :code")
    Optional<Category> findWithCategoryForUpdate(@Param("code") String code);

}
