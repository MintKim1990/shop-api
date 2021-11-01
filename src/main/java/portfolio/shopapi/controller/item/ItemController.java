package portfolio.shopapi.controller.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.request.item.StockItemRequest;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.request.item.clothes.SaveClothesRequest;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.service.item.ItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class ItemController {

    private final Map<String, ItemService> itemServiceMap;

    @PostMapping("/save/book")
    public Object saveBook(@RequestBody @Validated SaveBookRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService bookService = itemServiceMap.get("bookService");
            return bookService.saveItem(request);
        }
    }

    @PutMapping("/quantity/add/book")
    public Object quantityAddBook(@RequestBody @Validated StockItemRequest stockItemRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService bookService = itemServiceMap.get("bookService");
            return bookService.addQuantity(stockItemRequest);
        }
    }

    @PutMapping("/quantity/discount/book")
    public Object quantityDiscountBook(@RequestBody @Validated StockItemRequest stockItemRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService bookService = itemServiceMap.get("bookService");
            return bookService.discountQuantity(stockItemRequest);
        }
    }

    @PostMapping("/save/clothes")
    public Object saveClothes(@RequestBody @Validated SaveClothesRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService clothesService = itemServiceMap.get("clothesService");
            return clothesService.saveItem(request);
        }
    }

    @PutMapping("/quantity/add/clothes")
    public Object quantityAddClothes(@RequestBody @Validated StockItemRequest stockItemRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService clothesService = itemServiceMap.get("clothesService");
            return clothesService.addQuantity(stockItemRequest);
        }
    }

    @PutMapping("/quantity/discount/clothes")
    public Object quantityDiscountClothes(@RequestBody @Validated StockItemRequest stockItemRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            ItemService clothesService = itemServiceMap.get("clothesService");
            return clothesService.discountQuantity(stockItemRequest);
        }
    }

}
