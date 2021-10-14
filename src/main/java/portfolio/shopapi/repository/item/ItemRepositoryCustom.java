package portfolio.shopapi.repository.item;

import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.response.item.book.BookResponse;

import java.util.List;

public interface ItemRepositoryCustom {
    List<BookResponse> findAutobiography();
}
