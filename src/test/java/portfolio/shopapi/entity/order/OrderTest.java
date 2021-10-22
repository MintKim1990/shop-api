package portfolio.shopapi.entity.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.request.item.SaveAutobiographyRequest;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.response.order.OrderResponse;
import portfolio.shopapi.service.item.ItemService;
import portfolio.shopapi.service.member.MemberService;
import portfolio.shopapi.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class OrderTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    /**
     * 주문테스트
     */
    @Test
    @Rollback(value = false)
    public void orderTest() {

        Member member = Member.CreateMember(
                new MemberSaveRequest(
                        "테스트",
                        "서울",
                        "강서구",
                        "123",
                        "01071656293"
                )
        );

        // Spring Jpa Data 기본 메서드 (SimpleJpaRepository.save)
        Member saveMember = memberRepository.save(member);

        Item item = Autobiography.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Item saveItem = itemRepository.save(item);

        List<Items> items = new ArrayList<>();

        items.add(
                Items.builder()
                        .itemId(saveItem.getId())
                        .itemCount(10)
                        .build()
        );

        Order order = orderService.order(
                saveMember.getId(),
                items
        );

        System.out.println("order = " + order);

    }

    /**
     * 멀티스레드 주문테스트
     */
    @Test
    @Rollback(value = false)
    public void multiThreadOrderTest() throws InterruptedException {

        Member member = Member.CreateMember(
                new MemberSaveRequest(
                        "테스트",
                        "서울",
                        "강서구",
                        "123",
                        "01071656293"
                )
        );

        // Spring Jpa Data 기본 메서드 (SimpleJpaRepository.save)
        Member saveMember = memberRepository.save(member);

        Item item = Autobiography.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Item saveItem = itemRepository.save(item);

        // 멀티스레드 테스트
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        // 45
        for(int i = 0; i < 10; i++) {

            int discount = i;

            service.execute(() -> {
                try {

                    System.out.println("i = " + discount + " / discount = " + discount);

                    List<Items> items = new ArrayList<>();

                    items.add(
                            Items.builder()
                                    .itemId(saveItem.getId())
                                    .itemCount(discount)
                                    .build()
                    );

                    Order order = orderService.order(
                            saveMember.getId(),
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

    }

    @Test
    @Rollback(value = false)
    public void fetchJoinTest() throws InterruptedException {

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

        /*
            자식 카테고리들을 저장하는 로직이 존재하지 않는이유
            @OneToMany(mappedBy = "category_parent", cascade = CascadeType.ALL)
            CascadeType.ALL 옵션으로 Book 최상위 엔티티 아래로 자식 카테고리들은 자동추가되게 설정
         */
        categoryRepository.save(book);

        MemberSaveRequest memberSaveRequest = MemberSaveRequest.builder()
                .name("테스트")
                .city("서울")
                .street("강서구")
                .zipcode("123")
                .phone("01071656293")
                .build();

        Long memberid = memberService.saveMember(memberSaveRequest);

        SaveAutobiographyRequest request = SaveAutobiographyRequest.builder()
                .name("김민태는 왜 공부를 안하는가")
                .stockQuantity(100)
                .price(10000)
                .categoryCode("Book")
                .auther("김민태")
                .isbn("12-TB2")
                .build();

        Long itemId = itemService.saveAutobiography(request);

        SaveAutobiographyRequest request2 = SaveAutobiographyRequest.builder()
                .name("이제라도 공부하자..")
                .stockQuantity(10)
                .price(50000)
                .categoryCode("Book")
                .auther("김민태")
                .isbn("12-TB3")
                .build();

        Long itemId2 = itemService.saveAutobiography(request2);

        List<Items> items = new ArrayList<>();

        items.add(
                Items.builder()
                    .itemId(itemId)
                    .itemCount(10)
                    .build()
        );

        items.add(
                Items.builder()
                        .itemId(itemId2)
                        .itemCount(2)
                        .build()
        );

        orderService.order(
                memberid,
                items
        );

        List<OrderResponse> orders = orderService.findOrders(memberid);
        orders.stream().forEach( o -> System.out.println("Order = " + o));

    }

}