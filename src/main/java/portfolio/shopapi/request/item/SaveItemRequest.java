package portfolio.shopapi.request.item;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaveItemRequest {

    @NotBlank(message = "상품이름은 필수입니다.")
    private String name;

    @NotNull(message = "상품가격은 필수입니다.")
    private Integer price;

    @NotNull(message = "상품수량은 필수입니다.")
    private Integer stockQuantity;

    @NotEmpty(message = "카테고리는 한개 이상 입력해야합니다.")
    private List<String> categoryCodes;

    public SaveItemRequest(String name, Integer price, Integer stockQuantity, List<String> categoryCodes) {

        //Assert.notEmpty(categoryCodes, "카테고리 정보는 필수입니다");

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryCodes = categoryCodes;
    }
}
