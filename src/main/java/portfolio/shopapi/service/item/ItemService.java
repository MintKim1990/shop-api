package portfolio.shopapi.service.item;

import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.request.item.StockItemRequest;
import portfolio.shopapi.response.Response;

import java.util.Optional;

public interface ItemService<T, R> {

    R findById(Long id);

    Response saveItem(T request);

    R findWithItemForUpdate (Long id);

    Response discountQuantity(StockItemRequest request);

    Response addQuantity(StockItemRequest request);

}
