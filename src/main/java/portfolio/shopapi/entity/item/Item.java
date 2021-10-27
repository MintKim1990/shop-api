package portfolio.shopapi.entity.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.mapping.ItemCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemCategory> itemCategories = new ArrayList<>();

    /**
     * Item 생성 (상속관계에서만 호출가능)
     * @param name
     * @param price
     * @param stockQuantity
     */
    protected Item(String name, int price, int stockQuantity, List<ItemCategory> itemCategories) {

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;

        itemCategories.stream().forEach(this::addItemCategories);
    }

    // 양방향주입
    private void addItemCategories(ItemCategory itemCategory) {
        this.itemCategories.add(itemCategory);
        itemCategory.setItem(this);
    }

    /**
     * 재고증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고감소
     * @param quantity
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("재고 수량이 부족합니다.");
        }

        this.stockQuantity = restStock;
    }

}
