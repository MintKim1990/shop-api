package portfolio.shopapi.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import portfolio.shopapi.entity.item.Item;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * 각 상품별 레파지토리를 상속관계로 만든이유는 조회할때 Item 타입으로 조회하게되면 상속구조에 모든 상품을 조인하여
 * 제너릭 타입으로 선언하여 상품별 레파지토리를 따로 생성
 * @param <T>
 */
public interface ItemRepository<T extends Item> extends JpaRepository<T, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<T> findWithItemForUpdateById(Long id);

}
