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
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.repository.order.OrderRepository;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.response.order.OrderItemResponse;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Order order(Long memberId, List<Items> items) {

        List<OrderItem> orderItems = new ArrayList<>();

        // 사용자
        Member member = memberRepository.findMemberById(memberId);

        // 주문리스트 생성
        items.stream().forEach(item -> {

            // Lock 상태
            Item findItem = itemRepository.findWithItemForUpdate(item.getItemId());

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

    @Transactional
    public List<OrderResponse> findOrders(Long memberId) {

        /*
            주문조회시 조인되는 테이블은 회원, 주문리스트, 상품, 상품카테고리리스트, 카테고리등 많다.

            주문테이블을 메인테이블로 Fetch Join시 1:N 관계에 있는 테이블은
            주문리스트, 상품카테고리리스트가 존재하여 실질적으로는 1:N:M 관계가 된다.

            Fetch Join시 메인테이블기준 ToMany 관계에 테이블이 2개이상(1:N:M..) 존재하게될경우
            JPA는 DB에서 리턴한 1*N*M 데이터에서 N*M 만큼 주문PK가 동일한 데이터가 존재하게되므로
            N*M 주문데이터중 어떤 데이터를 주문엔티티에 넣어야하는지 알수없게되어 MultipleBagFetchException 에러를 발생시킨다.

            따라서 Fetch Join은 ToMany 관계가 1개일경우만 사용이 가능하다.

            그럼 지금처럼 주문테이블에 모든 데이터를 가져오기위해선 어떤 방법이 좋을까?

            1순위 : ToMany 관계가 1개일경우 Fetch Join 한방쿼리로 조회
            2순위 : 연관관계에 데이터를 지연로딩으로 처리후 default_batch_fetch_size를 적용하여 지연로딩되는 테이블을 IN절로 조회하여 N+1문제를 처리

            해결방안
            1. 주문:회원:주문리스트:아이템 까지는 Fetch Join 으로 데이터 조회
            2. Fetch Join 으로 조회된 결과에 상품카테고리리스트는 default_batch_fetch_size를 적용하여 지연로딩으로 조회
         */

        List<Order> orders = orderRepository.findOrders(memberId);

        return orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());

    }

}
