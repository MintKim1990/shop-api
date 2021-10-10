package portfolio.shopapi.entity.mapping;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory {

    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categories;

    private void setItem(Item item) {
        this.item = item;
    }

    private void setCategories(Category categories) {
        this.categories = categories;
    }

    public static ItemCategory createItemCategory(Item item, Category category) {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItem(item);
        itemCategory.setCategories(category);
        return itemCategory;
    }

}
