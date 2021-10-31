package portfolio.shopapi.service.item.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.repository.item.book.BookRepository;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.service.item.ItemService;
import portfolio.shopapi.service.itemcategory.ItemCategoryService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService implements ItemService<SaveBookRequest, Book> {

    private final ItemCategoryService categoryService;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * 도서 저장
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response saveItem(SaveBookRequest request) {

        // 카테고리 리스트 생성
        List<ItemCategory> itemCategories = categoryService.createItemCategories(
                request.getCategoryCodes()
        );

        // 도서상품 객체 생성
        Book book = Book.createBook(request, itemCategories);

        bookRepository.save(book);

        return new Response<BookResponse>(
                BookResponse.createBookResponse(book)
        );
    }


    /**
     * Item LOCK 조회
     * @param id
     * @return
     */
    @Override
    public Book findWithItemForUpdate (Long id) {
        return bookRepository.findWithItemForUpdateById(id);
    }

    /**
     * 수량차감 LOCK 테스트용
     * @param id
     * @param quantity
     * @return
     */
    @Transactional
    public Integer discountQuantity(Long id, int quantity) {

        Book findBook = bookRepository.findWithItemForUpdateById(id);

        if(Objects.nonNull(findBook)) {
            findBook.removeStock(quantity);
            return findBook.getStockQuantity();
        }

        return null;
    }

}
