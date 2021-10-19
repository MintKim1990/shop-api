package portfolio.shopapi.request.item;

import lombok.Builder;
import lombok.Data;

@Data
public class BookSaveRequest {

    private String name;
    private int price;
    private int stockQuantity;
    private String categoryCode;
    private String auther;
    private String isbn;

    @Builder
    public BookSaveRequest(String name, int price, int stockQuantity, String categoryCode, String auther, String isbn) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryCode = categoryCode;
        this.auther = auther;
        this.isbn = isbn;
    }
}
