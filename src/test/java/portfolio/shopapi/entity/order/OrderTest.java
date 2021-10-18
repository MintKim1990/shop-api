package portfolio.shopapi.entity.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.dto.condition.CategoryCondition;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.response.category.CategoryListResponse;
import portfolio.shopapi.response.order.OrderResponse;
import portfolio.shopapi.service.order.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderTest {

    @Autowired
    OrderService orderService;

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

        Order order = orderService.order(
                saveMember.getId(),
                saveItem.getId(),
                10
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

                    Order order = orderService.order(
                            saveMember.getId(),
                            saveItem.getId(),
                            discount
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

        Order order = orderService.order(
                saveMember.getId(),
                saveItem.getId(),
                10
        );

        List<OrderResponse> orders = orderService.findOrders(saveMember.getId());
        orders.stream().forEach(System.out::println);

    }

}