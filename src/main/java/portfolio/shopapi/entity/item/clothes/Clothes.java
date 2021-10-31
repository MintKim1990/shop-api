package portfolio.shopapi.entity.item.clothes;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.constant.ClothesSize;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.request.item.clothes.SaveClothesRequest;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends Item {

    private String material; // 소재
    @Enumerated(EnumType.STRING)
    private ClothesSize size; // 길이

    @Builder
    public Clothes(String name, int price, int stockQuantity, List<ItemCategory> itemCategories, String material, ClothesSize size) {
        super(name, price, stockQuantity, itemCategories);
        this.material = material;
        this.size = size;
    }

    public static Clothes createClothes(SaveClothesRequest request, List<ItemCategory> itemCategories) {
        return Clothes.builder()
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .price(request.getPrice())
                .itemCategories(itemCategories)
                .material(request.getMaterial())
                .size(request.getSize())
                .build();
    }
}
