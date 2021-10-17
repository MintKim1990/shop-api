package portfolio.shopapi.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portfolio.shopapi.entity.item.Item;

import javax.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id = :id")
    Item findWithItemForUpdate(@Param("id") Long id);



}
