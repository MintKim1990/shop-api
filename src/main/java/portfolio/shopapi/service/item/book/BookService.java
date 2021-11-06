package portfolio.shopapi.service.item.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.constant.ResponseType;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.exception.BisnessParametersException;
import portfolio.shopapi.repository.item.book.BookRepository;
import portfolio.shopapi.request.item.StockItemRequest;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.service.item.ItemService;
import portfolio.shopapi.service.itemcategory.ItemCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements ItemService<SaveBookRequest, Book> {

    private final ItemCategoryService categoryService;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    throw new BisnessParametersException("도서가 조회되지 않습니다.");
                });
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
                ResponseType.SUCCESS,
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
        return bookRepository.findWithItemForUpdateById(id)
                .orElseThrow(() -> {
                    throw new BisnessParametersException("도서가 조회되지 않습니다.");
                });
    }

    /**
     * 수량 차감
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response discountQuantity(StockItemRequest request) {
        Book findBook = bookRepository.findWithItemForUpdateById(request.getId())
                .orElseThrow(() -> {
                    throw new BisnessParametersException("도서가 조회되지 않습니다.");
                });
        findBook.removeStock(request.getStockQuantity());

        return new Response<BookResponse>(
                ResponseType.SUCCESS,
                BookResponse.createBookResponse(findBook)
        );
    }

    /**
     * 수량 증가
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response addQuantity(StockItemRequest request) {
        Book findBook = bookRepository.findWithItemForUpdateById(request.getId())
                .orElseThrow(() -> {
                    throw new BisnessParametersException("도서가 조회되지 않습니다.");
                });
        findBook.addStock(request.getStockQuantity());

        return new Response<BookResponse>(
                ResponseType.SUCCESS,
                BookResponse.createBookResponse(findBook)
        );
    }


}
