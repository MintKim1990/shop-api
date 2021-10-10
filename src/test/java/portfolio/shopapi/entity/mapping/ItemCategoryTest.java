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
import portfolio.shopapi.repository.mapping.ItemCategoryRepository;

@SpringBootTest
class ItemCategoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemCategoryRepository itemCategoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    @Rollback(value = false)
    @Test
    public void itemCategoryTest() {

        Category category_Book = new Category("Book", "도서");
        Category category = categoryRepository.save(category_Book);

        Category category_Autobiography = new Category("autobiography", "자서전");
        Category save1 = categoryRepository.save(category_Autobiography);

        Category category_Major = new Category("major", "전공");
        Category save2 = categoryRepository.save(category_Major);

        category.addCategory(save1);
        category.addCategory(save2);

        Item autobiography = new Autobiography(
                "김민태의 일대기",
                10000,
                10,
                "김민태",
                "IS-19231-T"
        );

        Item item = itemRepository.save(autobiography);

        ItemCategory itemCategory = ItemCategory.createItemCategory(item, save1);
        itemCategoryRepository.save(itemCategory);

    }

}