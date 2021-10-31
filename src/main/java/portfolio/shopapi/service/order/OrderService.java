package portfolio.shopapi.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.mapping.OrderItem;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.item.book.BookRepository;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.repository.order.OrderRepository;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Order order(Long memberId, List<Items> items) {

        List<OrderItem> orderItems = new ArrayList<>();

        // 사용자
        Member member = memberRepository.findMemberById(memberId);

        // 주문리스트 생성
        items.stream().forEach(item -> {

            /*
                Item 엔티티는 @Inheritance(strategy = InheritanceType.JOINED) 조인전략을 사용하고있으며
                이는 상품 엔티티를 조회하는 과정에서 Item에 상속관계에있는 모든 엔티티에 조인이 걸려서
                상품이 늘어날수록 성능상에 이슈가 발생할 가능성이있다.

                이에 상품별로 생성한 레파지토리를 사용하여 조회시 연관관계에 있는 상품만 조인하기때문에
                성능상 유리하지만 주문하려는 상품이 어떤 상품인지 알아야하여
                주문시 Item 엔티티에 @DiscriminatorColumn 컬럼 데이터를 추가로 받아 상품을 식별하여
                레파지토리별로 조회할까 했는데...

                디자인패턴을 공부하면 뭔가 더 좋은방법이 있을것같기도하고..
                일단 두고 성능상에 이슈가 생기면 고치는걸로 생각하여 냅두기로했다..
             */

            // Lock 상태
            Item findItem = itemRepository.findWithItemForUpdateById(item.getItemId());

            // Lock 상태에 Item에 수량 변경감지로 차감예정
            OrderItem orderItem = OrderItem.createOrderItem(findItem, item.getItemCount());

            orderItems.add(orderItem);

        });

        // 배송
        Delivery delivery = Delivery.createDelivery(member);

        // 주문
        Order order = Order.createOrder(
                member,
                delivery,
                orderItems
        );

        return orderRepository.save(order);

    }

    /**
     * QueryDsl 조회 후 default_batch_fetch_size를 적용한 지연로딩방식
     *
     * Fetch Join시 메인테이블기준 ToMany 관계에 테이블이 2개이상(1:N:M..) 존재하게될경우
     * JPA는 DB에서 리턴한 1*N*M 데이터에서 N*M 만큼 주문PK가 동일한 데이터가 존재하게되므로
     * N*M 주문데이터중 어떤 데이터를 주문엔티티에 넣어야하는지 알수없게되어 MultipleBagFetchException 에러를 발생시킨다.
     *
     * 처리방식
     * 1. 주문:회원:주문리스트:아이템 까지는 Fetch Join 으로 데이터 조회 (ToMany 관계는 주문리스트 한개)
     * 2. Fetch Join 으로 조회된 결과에 카테고리정보는 default_batch_fetch_size를 적용하여 지연로딩으로 조회
     *
     * @param memberId
     * @return
     */
    @Transactional
    public List<OrderResponse> findOrdersByQueryDsl(Long memberId) {

        /*
            주문조회시 조인되는 테이블은 회원, 주문리스트, 상품, 상품카테고리리스트, 카테고리등 많다.

            주문테이블을 메인테이블로 Fetch Join시 ToMany 관계에 있는 테이블은
            주문리스트, 상품카테고리리스트가 존재하여 실질적으로는 1:N:M 관계가 된다.

            Fetch Join시 메인테이블기준 ToMany 관계에 테이블이 2개이상(1:N:M..) 존재하게될경우
            JPA는 DB에서 리턴한 1*N*M 데이터에서 N*M 만큼 주문PK가 동일한 데이터가 존재하게되므로
            N*M 주문데이터중 어떤 데이터를 주문엔티티에 넣어야하는지 알수없게되어 MultipleBagFetchException 에러를 발생시킨다.

            따라서 Fetch Join은 ToMany 관계가 1개일경우만 사용이 가능하다.

            그럼 지금처럼 주문테이블에 모든 데이터를 가져오기위해선 어떤 방법이 좋을까?

            1순위 : ToMany 관계가 1개일경우 Fetch Join 한방쿼리로 조회
            2순위 : 연관관계에 데이터를 지연로딩으로 처리후 default_batch_fetch_size를 적용하여 지연로딩되는 테이블을 IN절로 조회하여 N+1문제를 처리

            해결방안
            1. 주문:회원:주문리스트:아이템 까지는 Fetch Join 으로 데이터 조회 (ToMany 관계는 주문리스트 한개)
            2. Fetch Join 으로 조회된 결과에 상품카테고리리스트는 default_batch_fetch_size를 적용하여 지연로딩으로 조회
         */

        List<Order> orders = orderRepository.findOrders(memberId);

        return orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());

    }

    /**
     * Spring Data Jpa @Query 어노테이션 방식
     *
     * Fetch Join시 메인테이블기준 ToMany 관계에 테이블이 2개이상(1:N:M..) 존재하게될경우
     * JPA는 DB에서 리턴한 1*N*M 데이터에서 N*M 만큼 주문PK가 동일한 데이터가 존재하게되므로
     * N*M 주문데이터중 어떤 데이터를 주문엔티티에 넣어야하는지 알수없게되어 MultipleBagFetchException 에러를 발생시킨다.
     *
     * 처리방식
     * 1. 주문:회원:주문리스트:아이템 까지는 Fetch Join 으로 데이터 조회 (ToMany 관계는 주문리스트 한개)
     * 2. 조회된 결과에 상품 엔티티에 PK를 가지고 나머지 ToMany 관계(상품카테고리, 카테고리)를 추가조회하여 세팅
     *
     * findOrdersByQueryDsl 메소드 방식은 상품카테고리리스트, 카테고리 관계를 2번조회하는데 반에
     * findOrdersBySpringDataJPA 메소드 방식은 상품카테고리리스트, 카테고리 관계를 FetchJoin 하여 상품 PK로 조회하여 한번실행
     *
     * @param memberId
     * @return
     */
    @Transactional
    public List<OrderResponse> findOrdersBySpringDataJPA(Long memberId) {

        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);

        // 주문:회원:주문리스트:아이템 관계를 FetchJoin 으로 조회
        List<OrderResponse> orderResponses = orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());

        // 조회된 FetchJoin 결과에 상품엔티티 PK 기준으로 ToMany(카테고리:상품카테고리) 관계에 데이터 Fetchjoin 조회하여 세팅
        orderResponses.forEach(orderResponse -> {
            orderResponse.getItems().forEach(orderItemResponse -> {
                orderItemResponse.setCategoryCode(
                        orderRepository.findOrderCategorysByItemId(orderItemResponse.getItemId())
                        .stream()
                        .map(category -> category.getCode())
                        .collect(Collectors.toList())
                );
            });
        });

        return orderResponses;

    }

}
