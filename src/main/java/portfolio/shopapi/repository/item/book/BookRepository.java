package portfolio.shopapi.repository.item.book;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.repository.item.ItemRepository;

public interface BookRepository extends ItemRepository<Book> {
}
