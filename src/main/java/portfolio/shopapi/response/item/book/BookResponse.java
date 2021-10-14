package portfolio.shopapi.response.item.book;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public BookResponse(Long id, String name, int price, int stockQuantity, String auther, String isbn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.auther = auther;
        this.isbn = isbn;
    }
}
