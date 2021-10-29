package portfolio.shopapi.service.item;

import portfolio.shopapi.entity.item.Item;

import java.util.Optional;

public interface ItemService<T> {

    Optional<Item> findById(Long id);

    Long saveItem(T request);

    Item findWithItemForUpdate (Long id);

}
