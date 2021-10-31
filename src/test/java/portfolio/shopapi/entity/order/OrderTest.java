package portfolio.shopapi.entity.order;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import portfolio.shopapi.entity.constant.ClothesSize;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.item.clothes.Clothes;
import portfolio.shopapi.request.category.CategorySet;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.request.item.clothes.SaveClothesRequest;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.response.item.clothes.ClothesResponse;
import portfolio.shopapi.response.order.OrderResponse;
import portfolio.shopapi.service.category.CategoryService;
import portfolio.shopapi.service.item.book.BookService;
import portfolio.shopapi.service.item.clothes.ClothesService;
import portfolio.shopapi.service.member.MemberService;
import portfolio.shopapi.service.order.OrderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class OrderTest {

    @Autowired
    OrderService orderService;

    @Autowired
    BookService bookService;

    @Autowired
    ClothesService clothesService;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryService categoryService;

    @BeforeAll // 한번만실행
    public void beforeAll() {

        // 카테고리 데이터 저장
        categoryService.saveCategory(
                CategorySet.builder().code("Book").name("도서").build()
        );
        categoryService.saveCategory(
                CategorySet.builder().code("Autobiography").name("자서전").parent_code("Book").build()
        );
        categoryService.saveCategory(
                CategorySet.builder().code("Major").name("전공").parent_code("Book").build()
        );
        categoryService.saveCategory(
                CategorySet.builder().code("Electric").name("전기과").parent_code("Major").build()
        );
        categoryService.saveCategory(
                CategorySet.builder().code("Clothes").name("의류").build()
        );
    }


    /**
     * 주문테스트
     */
    @Test
    @Rollback(value = false)
    public void 주문() {

        Long memberid = memberService.saveMember(
                MemberSaveRequest.builder()
                        .name("테스트")
                        .city("서울")
                        .street("강서구")
                        .zipcode("123")
                        .phone("01071656293")
                        .build()
        );

        Response response = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        BookResponse bookResponse = (BookResponse) response.getData();

        Response response2 = clothesService.saveItem(
                SaveClothesRequest.builder()
                        .name("청바지")
                        .stockQuantity(10)
                        .price(50000)
                        .categoryCodes(Arrays.asList("Clothes"))
                        .material("천")
                        .size(ClothesSize.SMALL)
                        .build()
        );

        ClothesResponse clothesResponse = (ClothesResponse) response2.getData();


        List<Items> items = Arrays.asList(
                new Items(bookResponse.getId(), 10),
                new Items(clothesResponse.getId(), 1)
        );

        orderService.order(
                memberid,
                items
        );

        List<OrderResponse> orders = orderService.findOrdersBySpringDataJPA(memberid);
        assertThat(orders.size()).isEqualTo(1);

    }

    /**
     * 멀티스레드 주문테스트
     */
    @Test
    @Rollback(value = false)
    public void 멀티스레드_주문() throws InterruptedException {

        Long memberid = memberService.saveMember(
                MemberSaveRequest.builder()
                        .name("테스트")
                        .city("서울")
                        .street("강서구")
                        .zipcode("123")
                        .phone("01071656293")
                        .build()
        );

        Response response = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        BookResponse data = (BookResponse) response.getData();

        // 멀티스레드 테스트
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // 45
        for(int i = 0; i < 10; i++) {

            int discount = i;

            service.execute(() -> {
                try {

                    System.out.println("i = " + discount + " / discount = " + discount);

                    List<Items> items = Arrays.asList(
                            new Items(data.getId(), discount)
                    );

                    // 주문
                    Order order = orderService.order(
                            memberid,
                            items
                    );
                    latch.countDown();

                    System.out.println("i = " + discount + " / order = " + order);

                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.await();

        List<OrderResponse> orders = orderService.findOrdersBySpringDataJPA(memberid);

        Optional<Book> findItem = bookService.findById(data.getId());
        if(!findItem.isPresent()) fail("아이템이 존재하지 않습니다.");

        assertThat(orders.size()).isEqualTo(10);
        assertThat(findItem.get().getStockQuantity()).isEqualTo(55);

    }

    @Test
    @Rollback(value = false)
    public void 상품주문_결과조회() throws InterruptedException {

        Long memberid = memberService.saveMember(
                MemberSaveRequest.builder()
                        .name("테스트")
                        .city("서울")
                        .street("강서구")
                        .zipcode("123")
                        .phone("01071656293")
                        .build()
        );

        Response response = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book", "Autobiography", "Major"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        Response response2 = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("이제라도 공부하자..")
                        .stockQuantity(10)
                        .price(50000)
                        .categoryCodes(Arrays.asList("Book", "Autobiography"))
                        .auther("김민태")
                        .isbn("12-TB3")
                        .build()
        );

        BookResponse data = (BookResponse) response.getData();
        BookResponse data2 = (BookResponse) response2.getData();

        List<Items> firstOrderitems = Arrays.asList(
                new Items(data.getId(), 10),
                new Items(data2.getId(), 2)
        );

        // 주문
        orderService.order(memberid, firstOrderitems);

        List<OrderResponse> orders = orderService.findOrdersBySpringDataJPA(memberid);

        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getItems().get(0).getStockQuantity()).isEqualTo(90);
        assertThat(orders.get(0).getItems().get(1).getStockQuantity()).isEqualTo(8);

    }

    @Test
    @Rollback(value = false)
    public void 멀티스레드_상품주문_결과조회() throws InterruptedException {

        // 회원 저장
        Long saveMemberId = memberService.saveMember(
                MemberSaveRequest.builder()
                        .name("테스트")
                        .city("서울")
                        .street("강서구")
                        .zipcode("123")
                        .phone("01071656293")
                        .build()
        );

        Response response = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book", "Autobiography"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        Response response2 = bookService.saveItem(
                SaveBookRequest.builder()
                        .name("이제라도 공부하자..")
                        .stockQuantity(100)
                        .price(50000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB3")
                        .build()
        );

        BookResponse data = (BookResponse) response.getData();
        BookResponse data2 = (BookResponse) response2.getData();

        // 멀티스레드 테스트
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // 45
        for(int i = 0; i < 10; i++) {

            int discount = i;

            service.execute(() -> {
                try {

                    // 주문리스트 생성
                    List<Items> items = Arrays.asList(
                            new Items(data.getId(), discount),
                            new Items(data2.getId(), discount)
                    );                   

                    // 주문
                    Order order = orderService.order(
                            saveMemberId,
                            items
                    );
                    
                    latch.countDown();


                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.await();

        List<OrderResponse> orders = orderService.findOrdersBySpringDataJPA(saveMemberId);
        orders.stream().forEach( o -> System.out.println("Order = " + o));

        Optional<Book> findItem1 = bookService.findById(data.getId());
        if(!findItem1.isPresent()) fail("아이템이 존재하지 않습니다.");

        Optional<Book> findItem2 = bookService.findById(data2.getId());
        if(!findItem2.isPresent()) fail("아이템이 존재하지 않습니다.");

        assertThat(orders.size()).isEqualTo(10);
        assertThat(findItem1.get().getStockQuantity()).isEqualTo(55);
        assertThat(findItem2.get().getStockQuantity()).isEqualTo(55);

    }

}