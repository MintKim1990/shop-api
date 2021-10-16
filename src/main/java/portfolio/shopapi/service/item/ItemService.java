package portfolio.shopapi.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.repository.item.ItemRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Integer discountQuantity(Long id, int quantity) {

        Item findItem = itemRepository.findWithQauntityForUpdate(id);

        if(Objects.nonNull(findItem)) {
            findItem.removeStock(quantity);
            return findItem.getStockQuantity();
        }

        return null;
    }

}
