package portfolio.shopapi.service.item;

import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.response.Response;

import java.util.Optional;

public interface ItemService<T, R> {

    Optional<R> findById(Long id);

    Response saveItem(T request);

    Item findWithItemForUpdate (Long id);

}
