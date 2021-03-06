package portfolio.shopapi.entity.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.constant.ClothesSize;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.item.clothes.Clothes;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.book.BookRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.item.clothes.ClothesRepository;
import portfolio.shopapi.request.item.StockItemRequest;
import portfolio.shopapi.service.item.book.BookService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class ItemTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ClothesRepository clothesRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookService bookService;

    @Test
    public void discountQuantityTest() throws InterruptedException {

        Category book = categoryRepository.save(
                new Category("Book", "도서")
        );

        // Item 을 생성하여 처리하는동안 Category 가 변경될경우 데이터 정합성에 문제가 생길수있어 비관적 Lock 처리
        Category findCategory = categoryRepository.findById(book.getCode()).get();

        List<ItemCategory> itemCategory = Arrays.asList(
                ItemCategory.createItemCategory(findCategory)
        );

        Book bookItem = Book.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .itemCategories(itemCategory)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        bookRepository.save(bookItem);

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // 45
        for(int i = 0; i < 10; i++) {

            int discount = i;

            service.execute(() -> {
                try {

                    bookService.discountQuantity(
                            StockItemRequest.builder()
                                    .id(bookItem.getId())
                                    .stockQuantity(discount)
                                    .build()
                    );

                    latch.countDown();

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

        Optional<Book> result = bookRepository.findById(bookItem.getId());
        if (result.isPresent()) {
            assertThat(55).isEqualTo(result.get().getStockQuantity());
        } else {
            fail("아이템이 존재해야한다.");
        }


    }

    @Test
    public void insertItemTest() {

        Category book = categoryRepository.save(
                new Category("Book", "도서")
        );

        List<ItemCategory> itemCategory = Arrays.asList(
                ItemCategory.createItemCategory(book)
        );

        Book bookItem = Book.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .itemCategories(itemCategory)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        bookRepository.save(bookItem);

        Optional<Book> findItem = bookRepository.findById(bookItem.getId());
        if (findItem.isPresent()) {
            System.out.println("findItem = " + findItem.get());
        } else {
            fail("아이템이 존재해야한다.");
        }
    }

    @Test
    public void multiItemTest() {

        Category book = categoryRepository.save(
                new Category("Book", "도서")
        );

        List<ItemCategory> bookItemCategory = Arrays.asList(
                ItemCategory.createItemCategory(book)
        );

        Book bookItem = Book.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .itemCategories(bookItemCategory)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        bookRepository.save(bookItem);



        Category clothes = categoryRepository.save(
                new Category("Clothes", "도서")
        );

        List<ItemCategory> clothesItemCategory = Arrays.asList(
                ItemCategory.createItemCategory(clothes)
        );

        Clothes clothesItem = Clothes.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .itemCategories(clothesItemCategory)
                .material("천")
                .size(ClothesSize.SMALL)
                .build();

        clothesRepository.save(clothesItem);


        Optional<Book> findBookItem = bookRepository.findById(bookItem.getId());
        Optional<Clothes> findClothesItem = clothesRepository.findById(clothesItem.getId());

        System.out.println("findBookItem.get() = " + findBookItem.get());
        System.out.println("findClothesItem.get() = " + findClothesItem.get());

    }

}