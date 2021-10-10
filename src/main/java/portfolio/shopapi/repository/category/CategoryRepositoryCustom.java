package portfolio.shopapi.repository.category;

import portfolio.shopapi.dto.condition.CategoryCondition;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.response.category.CategoryListResponse;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryListResponse> findCategoryList(CategoryCondition condition);
}
