package portfolio.shopapi.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.request.category.CategorySet;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveCategory(CategorySet categorySet) {

        Optional<Category> findCategory = categoryRepository.findById(categorySet.getCode());

        if (findCategory.isPresent()) {
            throw new ParameterException("이미 존재하는 카테고리 입니다.");
        } else {
            Category category = insertCategory(categorySet);
            return category.getCode();
        }
    }
    
    @Transactional
    public String updateCategory(CategorySet categorySet) {
        // 동시에 카테고리를 수정할 수 있으므로 Lock 처리
        Optional<Category> findCategory = categoryRepository.findWithCategoryForUpdate(categorySet.getCode());

        if (findCategory.isPresent()) {
            Category category = updateCategory(categorySet, findCategory);
            return category.getCode();
        } else {
            throw new ParameterException("존재하지 않는 카테고리 입니다.");
        }
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
     * @param findCategory
     * @return
     */
    private Category updateCategory(CategorySet categorySet, Optional<Category> findCategory) {

        // 변경감지를 이용한 이름 업데이트 처리
        Category category = findCategory.get();

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
        Optional<Category> findParentCategory = categoryRepository.findWithCategoryForUpdate(parentCode);

        if(findParentCategory.isPresent()) {
            findParentCategory.get().addChildCategory(childCategory);
        } else {
            throw new ParameterException("부모 카테고리가 존재하지 않습니다.");
        }

    }
}
