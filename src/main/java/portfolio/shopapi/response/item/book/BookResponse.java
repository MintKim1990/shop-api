package portfolio.shopapi.response.item.book;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.item.book.Book;

@Data
@NoArgsConstructor
public class BookResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String auther;
    private String isbn;

    @Builder
    private BookResponse(Long id, String name, int price, int stockQuantity, String auther, String isbn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.auther = auther;
        this.isbn = isbn;
    }

    public static BookResponse createBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .auther(book.getAuther())
                .isbn(book.getIsbn())
                .build();
    }
}
