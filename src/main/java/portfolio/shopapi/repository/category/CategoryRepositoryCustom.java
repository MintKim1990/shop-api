package portfolio.shopapi.repository.category;

import com.querydsl.core.Tuple;
import portfolio.shopapi.dto.condition.CategoryCondition;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.response.category.CategoryCountListResponse;
import portfolio.shopapi.response.category.CategoryListResponse;

import java.util.List;

public interface CategoryRepositoryCustom {

    /**
     * Book 카테고리 아래로 등록된 자식 카테고리 리스트 추출
     */
    List<CategoryListResponse> findCategoryList(CategoryCondition condition);

    /**
     * 카테고리별 자식카테고리 보유 건수
     */
    List<CategoryCountListResponse> findReliationCountCategory();
}
