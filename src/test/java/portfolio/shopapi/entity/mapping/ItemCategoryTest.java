package portfolio.shopapi.entity.mapping;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemCategoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    @Test
    public void itemCategoryTest() {

        Category book = categoryRepository.save(
                new Category("Book", "도서")
        );

        Category Autobiography = categoryRepository.save(
                new Category("Autobiography", "자서전")
        );

        Category Major = categoryRepository.save(
                new Category("Major", "전공")
        );

        book.addChildCategory(Autobiography);
        book.addChildCategory(Major);

        List<ItemCategory> itemCategory = Arrays.asList(
                ItemCategory.createItemCategory(Autobiography)
        );

        Item autobiography = new Autobiography(
                "김민태의 일대기",
                10000,
                10,
                itemCategory,
                "김민태",
                "IS-19231-T"
        );

        /*
            @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
            CascadeType.ALL 옵션으로 아이템 저장시 Category도 같이 저장
         */

        Item item = itemRepository.save(autobiography);
        assertThat(item.getItemCategories().size()).isEqualTo(1);

    }

}