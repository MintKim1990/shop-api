package portfolio.shopapi.entity.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.response.item.book.BookResponse;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @Rollback(value = false)
    public void perpetuity() {

        Item item = Autobiography.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Item saveItem1 = itemRepository.save(item);

        Item saveItem2 = itemRepository.findAll().get(0);

        saveItem1.removeStock(20);

        Item saveItem3 = itemRepository.findAll().get(0);

        System.out.println("saveItem2 = " + saveItem2.getStockQuantity());
        System.out.println("saveItem3 = " + saveItem3.getStockQuantity());

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void insertItemTest() {

        Item item = Autobiography.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Item saveItem = itemRepository.save(item);

        List<BookResponse> bookResponseList = itemRepository.findAutobiography();

        for (BookResponse bookResponse : bookResponseList) {
            System.out.println("bookResponse = " + bookResponse);
        }

    }

}