package portfolio.shopapi.entity.category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.mapping.ItemCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_id")
    private String code;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id")
    private Category category_parent;

    @OneToMany(mappedBy = "category_parent", cascade = CascadeType.ALL)
    private List<Category> category_child = new ArrayList<>();

    public Category(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void addChildCategory(Category category) {
        this.category_child.add(category);
        category.setCategory_parent(this);
    }

    public void addItemCategories(ItemCategory itemCategory) {
        this.itemCategories.add(itemCategory);
    }

    private void setCategory_parent(Category category_parent) {
        this.category_parent = category_parent;
    }
}
