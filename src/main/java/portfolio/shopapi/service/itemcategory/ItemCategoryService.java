package portfolio.shopapi.service.itemcategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.exception.BisnessParametersException;
import portfolio.shopapi.repository.category.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemCategoryService {

    private final CategoryRepository categoryRepository;

    public List<ItemCategory> createItemCategories (List<String> categoryCodes) {

        // Item 을 생성하여 처리하는동안 Category 가 변경될경우 데이터 정합성에 문제가 생길수있어 비관적 Lock 처리
        return categoryCodes.stream()
                .map(code -> {
                    Optional<Category> category = categoryRepository.findWithCategoryForUpdate(code);

                    if(category.isPresent()) {
                        return ItemCategory.createItemCategory(
                                category.get()
                        );
                    } else {
                        throw new BisnessParametersException("유효하지 않은 카테고리 코드입니다.");
                    }
                })
                .collect(Collectors.toList());

    }

}
