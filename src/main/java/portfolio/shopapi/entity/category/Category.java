package portfolio.shopapi.entity.category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import portfolio.shopapi.entity.BaseEntity;
import portfolio.shopapi.entity.mapping.ItemCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "category_id")
    private String code;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id")
    private Category category_parent;

    @OneToMany(mappedBy = "category_parent")
    private List<Category> category_child = new ArrayList<>();

    public Category(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void changeName(String name) {
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

    /*
        Category 엔티티는 PK가 String 이며
        Category 를 생성하는시점에 이미 할당된다.

        JpaRepository.save 메소드를 호출하게될경우
        isNew() 메소드 반환이 (PK컬럼이 객체일경우 : null / primitive : 0) 이면 persist
        아닐경우 merge가 실행되는데

        우리는 String code 에 객체타입으로 값을 지정했기때문에 isNew() 에서는 false가 반환되어
        merge가 실행되어 Select Insert가 실행된다.

        따라서 Persistable 인터페이스를 상속받아 isNew() 메소드를 오버라이딩하여
        해당 객체가 신규인지 기존인지 판단로직을 넣어주면 persist 형식으로 동작한다.

        여기서는 BaseEntity로 상속받은 createDate가 null일경우 신규로 판단하여 처리
     */
    @Override
    public String getId() {
        return code;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(super.getCreatedDate());
    }
}
