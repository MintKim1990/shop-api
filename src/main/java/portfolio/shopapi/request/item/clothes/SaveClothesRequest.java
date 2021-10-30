package portfolio.shopapi.request.item.clothes;

import lombok.Getter;
import lombok.Setter;
import portfolio.shopapi.entity.constant.ClothesSize;
import portfolio.shopapi.request.item.SaveItemRequest;

import java.util.List;

@Getter
@Setter
public class SaveClothesRequest extends SaveItemRequest {

    private String material; // 소재
    private ClothesSize size; // 길이

    public SaveClothesRequest(String name,
                              Integer price,
                              Integer stockQuantity,
                              List<String> categoryCodes,
                              String material,
                              ClothesSize size) {
        super(name, price, stockQuantity, categoryCodes);
        this.material = material;
        this.size = size;
    }
}
