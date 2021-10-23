package portfolio.shopapi.request.item;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;

@Data
public class SaveAutobiographyRequest {

    private String name;
    private int price;
    private int stockQuantity;
    private List<String> categoryCodes;
    private String auther;
    private String isbn;

    @Builder
    public SaveAutobiographyRequest(String name, int price, int stockQuantity, List<String> categoryCodes, String auther, String isbn) {

        Assert.notEmpty(categoryCodes, "카테고리 정보는 필수입니다");

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryCodes = categoryCodes;
        this.auther = auther;
        this.isbn = isbn;
    }
}
