package portfolio.shopapi.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.embedded.Delivery;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.mapping.OrderItem;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.repository.order.OrderRepository;
import portfolio.shopapi.response.order.OrderResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Order order(Long memberId, Long itemId, int itemCount) {

        // 사용자
        Member member = memberRepository.findMemberById(memberId);

        // Lock 상태
        Item item = itemRepository.findWithItemForUpdate(itemId);

        // Lock 상태에 Item에 수량 변경감지로 차감예정
        OrderItem orderItem = OrderItem.createOrderItem(item, itemCount);

        // 배송
        Delivery delivery = Delivery.createDelivery(member);

        // 주문
        Order order = Order.createOrder(
                member,
                delivery,
                orderItem
        );

        return orderRepository.save(order);

    }

    @Transactional
    public List<OrderResponse> findOrders(Long memberId) {
        List<OrderResponse> orders = orderRepository.findOrders(memberId);
        return orders;
    }

}
