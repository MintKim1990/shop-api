package portfolio.shopapi.entity.order;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.request.item.SaveAutobiographyRequest;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.response.order.OrderResponse;
import portfolio.shopapi.service.category.CategoryService;
import portfolio.shopapi.service.item.ItemService;
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

@SpringBootTest
class OrderTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setCategory() {
        // 카테고리 데이터 저장
        categoryRepository.save(
                getCategory()
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

        Long itemId = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        List<Items> items = Arrays.asList(
                new Items(itemId, 10)
        );

        Order order = orderService.order(
                memberid,
                items
        );

        System.out.println("order = " + order);

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

        Long itemId = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

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
                            new Items(itemId, discount)
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

        List<OrderResponse> orders = orderService.findOrders(memberid);

        Optional<Item> findItem = itemRepository.findById(itemId);
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

        Long itemId = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        Long itemId2 = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("이제라도 공부하자..")
                        .stockQuantity(10)
                        .price(50000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB3")
                        .build()
        );

        List<Items> firstOrderitems = Arrays.asList(
                new Items(itemId, 10),
                new Items(itemId2, 2)
        );

        // 주문
        orderService.order(memberid, firstOrderitems);

        List<OrderResponse> orders = orderService.findOrders(memberid);

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

        Long itemId = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("김민태는 왜 공부를 안하는가")
                        .stockQuantity(100)
                        .price(10000)
                        .categoryCodes(Arrays.asList("Book", "Autobiography"))
                        .auther("김민태")
                        .isbn("12-TB2")
                        .build()
        );

        Long itemId2 = itemService.saveAutobiography(
                SaveAutobiographyRequest.builder()
                        .name("이제라도 공부하자..")
                        .stockQuantity(100)
                        .price(50000)
                        .categoryCodes(Arrays.asList("Book"))
                        .auther("김민태")
                        .isbn("12-TB3")
                        .build()
        );

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
                            new Items(itemId, discount),
                            new Items(itemId2, discount)
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

        List<OrderResponse> orders = orderService.findOrders(saveMemberId);
        orders.stream().forEach( o -> System.out.println("Order = " + o));

        Optional<Item> findItem1 = itemRepository.findById(itemId);
        if(!findItem1.isPresent()) fail("아이템이 존재하지 않습니다.");

        Optional<Item> findItem2 = itemRepository.findById(itemId2);
        if(!findItem2.isPresent()) fail("아이템이 존재하지 않습니다.");

        assertThat(orders.size()).isEqualTo(10);
        assertThat(findItem1.get().getStockQuantity()).isEqualTo(55);
        assertThat(findItem2.get().getStockQuantity()).isEqualTo(55);

    }

    /**
     * 카테고리 테스트 데이터 생성
     * @return
     */
    private Category getCategory() {
        // 카테고리
        Category book = new Category("Book", "도서");
        Category autobiography = new Category("Autobiography", "자서전");
        Category major = new Category("Major", "전공");
        Category electric = new Category("Electric", "전기과");

        // 도서 카테고리 아래에 자서전, 전공 추가
        book.addChildCategory(autobiography);
        book.addChildCategory(major);

        // 자서전 카테고리 아래에 전기과 추가
        major.addChildCategory(electric);
        return book;
    }

}