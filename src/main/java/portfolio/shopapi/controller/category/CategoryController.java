package portfolio.shopapi.controller.category;

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
import portfolio.shopapi.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/save")
    public Object save(@RequestBody @Validated CategorySet categorySet, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return categoryService.saveCategory(categorySet);
        }
    }

    @PostMapping("/update")
    public Object update(@RequestBody @Validated CategorySet categorySet, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return categoryService.updateCategory(categorySet);
        }
    }

}
