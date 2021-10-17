package portfolio.shopapi.entity.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.service.item.ItemService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Test
    @Rollback(value = false)
    public void discountQuantityTest() throws InterruptedException {

        Item item = Autobiography.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Item saveItem = itemRepository.save(item);

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // 45
        for(int i = 0; i < 10; i++) {

            int discount = i;

            service.execute(() -> {
                try {

                    System.out.println("i = " + discount + " / discount = " + discount);

                    Integer quantity = itemService.discountQuantity(
                            saveItem.getId(),
                            discount
                    );
                    latch.countDown();

                    System.out.println("i = " + discount + " / quantity = " + quantity);

                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.await();

        /*

            여러 사용자가 동시에 Item을 조회 후 Item에 수량을 차감할경우
            순차적으로 차감했을경우 45개가 차감되야하나
            동시에 Item을 SELECT 하게될경우 순차적으로 처리되지않기때문에 데이터 정합성을 보증하지못한다.

            LOCK 으로 제어가 필요

            itemService.discountQuantity 메소드는 내부적으로
            itemRepository.findWithQauntityForUpdate 메소드를 호출하는데

            Item을 조회하는 시점에 @Lock(LockModeType.PESSIMISTIC_WRITE) 어노테이션으로
            LOCK처리를 해줘서 데이터 정합성을 보장

         */

        Item result = itemRepository.findById(saveItem.getId()).orElse(null);
        Assertions.assertThat(55).isEqualTo(result.getStockQuantity());

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