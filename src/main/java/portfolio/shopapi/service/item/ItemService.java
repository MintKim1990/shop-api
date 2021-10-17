package portfolio.shopapi.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.repository.item.ItemRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Item LOCK 조회
     * @param id
     * @return
     */
    public Item findWithItemForUpdate (Long id) {
        return itemRepository.findWithItemForUpdate(id);
    }

    /**
     * 수량차감 LOCK 테스트용
     * @param id
     * @param quantity
     * @return
     */
    @Transactional
    public Integer discountQuantity(Long id, int quantity) {

        Item findItem = itemRepository.findWithItemForUpdate(id);

        if(Objects.nonNull(findItem)) {
            findItem.removeStock(quantity);
            return findItem.getStockQuantity();
        }

        return null;
    }

}
