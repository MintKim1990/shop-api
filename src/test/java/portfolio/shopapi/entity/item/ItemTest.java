package portfolio.shopapi.entity.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.entity.item.book.QAutobiography;
import portfolio.shopapi.repository.item.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

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

        saveItem.addStock(10);
        saveItem.removeStock(20);

        List<Item> autobiographyList = itemRepository.findAutobiography();

        for (Item autobiography : autobiographyList) {
            System.out.println("autobiography = " + autobiography);
        }

    }

}