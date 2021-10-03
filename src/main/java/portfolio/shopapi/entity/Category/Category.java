package portfolio.shopapi.entity.Category;

import lombok.Getter;
import portfolio.shopapi.entity.mapping.ItemCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categories")
    private List<ItemCategory> itemCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id")
    private Category category_parent;

    @OneToMany(mappedBy = "category_parent")
    private List<Category> category_child = new ArrayList<>();

    public void addCategory(Category category) {
        this.category_child.add(category);
        category_parent = this;
    }

}
