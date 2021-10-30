package portfolio.shopapi.entity.item.book;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.mapping.ItemCategory;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

    private String auther;
    private String isbn;

    @Builder
    public Book(String name,
                int price,
                int stockQuantity,
                List<ItemCategory> itemCategories,
                String auther,
                String isbn) {
        super(name, price, stockQuantity, itemCategories);
        this.auther = auther;
        this.isbn = isbn;
    }

}
