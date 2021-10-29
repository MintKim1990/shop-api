package portfolio.shopapi.request.item.book;

import lombok.*;
import org.springframework.util.Assert;
import portfolio.shopapi.request.item.SaveItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class SaveBookRequest extends SaveItemRequest {

    @NotBlank(message = "저자은 필수입니다.")
    private String auther;

    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;

    @Builder
    public SaveBookRequest(String name, Integer price, Integer stockQuantity, List<String> categoryCodes, String auther, String isbn) {
        super(name, price, stockQuantity, categoryCodes);
        this.auther = auther;
        this.isbn = isbn;
    }
}
