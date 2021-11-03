package portfolio.shopapi.response.item.clothes;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.constant.ClothesSize;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.item.clothes.Clothes;
import portfolio.shopapi.response.item.book.BookResponse;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
public class ClothesResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String material;
    private ClothesSize size;

    @Builder
    private ClothesResponse(Long id, String name, int price, int stockQuantity, String material, ClothesSize size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.size = size;
    }

    public static ClothesResponse createClothesResponse(Clothes clothes) {
        return ClothesResponse.builder()
                .id(clothes.getId())
                .name(clothes.getName())
                .price(clothes.getPrice())
                .stockQuantity(clothes.getStockQuantity())
                .material(clothes.getMaterial())
                .size(clothes.getSize())
                .build();
    }
}
