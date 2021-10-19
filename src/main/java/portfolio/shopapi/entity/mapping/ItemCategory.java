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
    private Category category;

    public void setItem(Item item) {
        this.item = item;
    }

    /*
        @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
        setItem 세터로 CascadeType.ALL 옵션을통해 Item 생성시 연관관계로 인해 같이 처리가 되나
        Category 의 경우 조회하여 createItemCategory 메소드에 넣어줘야하므로 Item 을 생성하여 처리하는동안
        Category 가 변경될경우 데이터 정합성에 문제가 생길수있어 비관적 Lock 처리가 필요
     */
    public static ItemCategory createItemCategory(Category category) {

        ItemCategory itemCategory = new ItemCategory();

        itemCategory.category = category;

        return itemCategory;
    }

}
