package portfolio.shopapi.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import portfolio.shopapi.constant.ResponseType;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.exception.BisnessParametersException;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.request.category.CategorySet;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.category.CategoryResponse;
import portfolio.shopapi.response.item.book.BookResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Response saveCategory(CategorySet categorySet) {

        Optional<Category> findCategory = categoryRepository.findById(categorySet.getCode());

        if (findCategory.isPresent()) {
            throw new BisnessParametersException("이미 존재하는 카테고리 입니다.");
        } else {
            Category category = insertCategory(categorySet);
            return new Response<CategoryResponse>(
                    ResponseType.SUCCESS,
                    CategoryResponse.createCategoryResponse(category)
            );
        }
    }
    
    @Transactional
    public Response updateCategory(CategorySet categorySet) {
        // 동시에 카테고리를 수정할 수 있으므로 Lock 처리
        Category findCategory = categoryRepository.findWithCategoryForUpdate(categorySet.getCode())
                .orElseThrow(() -> {
                    throw new BisnessParametersException("존재하지 않는 카테고리 입니다.");
                });

        Category category = updateCategory(categorySet, findCategory);
        return new Response<CategoryResponse>(
                ResponseType.SUCCESS,
                CategoryResponse.createCategoryResponse(category)
        );
    }

    /**
     * 카테고리 신규저장
     * @param categorySet
     * @return
     */
    private Category insertCategory(CategorySet categorySet) {

        // 신규 저장
        Category category = categoryRepository.save(new Category(
                categorySet.getCode(),
                categorySet.getName()
        ));

        // 부모카테고리가 존재할경우 연관관계 설정
        if(StringUtils.hasText(categorySet.getParent_code())) {
            setParentCategory(categorySet.getParent_code(), category);
        }

        return category;
    }

    /**
     * 카테고리 업데이트
     * @param categorySet
     * @param category
     * @return
     */
    private Category updateCategory(CategorySet categorySet, Category category) {

        // 변경감지를 이용한 이름 업데이트 처리
        category.changeName(categorySet.getName());

        // 부모카테고리가 존재할경우 연관관계 설정
        if(StringUtils.hasText(categorySet.getParent_code())) {
            setParentCategory(categorySet.getParent_code(), category);
        }

        return category;
    }

    /**
     * 부모-자식 카테고리 연관관계 설정
     * @param parentCode
     * @param childCategory
     */
    private void setParentCategory (String parentCode, Category childCategory) {

        // 동시에 카테고리를 수정할 수 있으므로 Lock 처리
        Category findParentCategory = categoryRepository.findWithCategoryForUpdate(parentCode)
                .orElseThrow(() -> {
                    throw new BisnessParametersException("부모 카테고리가 존재하지 않습니다.");
                });

        findParentCategory.addChildCategory(childCategory);
    }
}
