package portfolio.shopapi.controller.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.request.category.CategorySet;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.service.item.ItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class ItemController {

    private final Map<String, ItemService> itemServiceMap;

    @PostMapping("/save/book")
    public Object save(@RequestBody @Validated SaveBookRequest request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService bookService = itemServiceMap.get("bookService");
            return bookService.saveItem(request);
        }
    }

}
