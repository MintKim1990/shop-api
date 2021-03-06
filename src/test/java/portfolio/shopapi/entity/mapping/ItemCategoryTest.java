package portfolio.shopapi.entity.mapping;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.item.book.BookRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemCategoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookRepository bookRepository;

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

        Book bookItem = new Book(
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

        bookRepository.save(bookItem);
        assertThat(bookItem.getItemCategories().size()).isEqualTo(1);

    }

}