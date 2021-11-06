package portfolio.shopapi.controller.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.request.order.Items;
import portfolio.shopapi.request.order.OrderItemRequest;
import portfolio.shopapi.service.item.ItemService;
import portfolio.shopapi.service.order.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save/{memberId}")
    public Object save(@PathVariable(name = "memberId") Long memberId,
                       @RequestBody @Validated OrderItemRequest itemsList,
                       BindingResult bindingResult) throws Throwable {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return orderService.order(memberId, itemsList.getItemsList());
        }
    }
}
