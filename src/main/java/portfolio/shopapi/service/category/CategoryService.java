package portfolio.shopapi.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.request.category.CategoryRequest;
import portfolio.shopapi.request.item.SaveAutobiographyRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveCategory(CategoryRequest categoryRequest) {

        Category category = new Category(
                categoryRequest.getCategory().getCode(),
                categoryRequest.getCategory().getName()
        );

        categoryRequest.getChild_category().stream()
                .forEach(categorySet -> category.addChildCategory(new Category(
                        categorySet.getCode(),
                        categorySet.getName()
                )));



        Optional<Category> categoryForUpdate = categoryRepository.findWithCategoryForUpdate(category.getCode());

        if(categoryForUpdate.isPresent()) {
            
        } else {

            /*
                자식 카테고리들을 저장하는 로직이 존재하지 않는이유
                @OneToMany(mappedBy = "category_parent", cascade = CascadeType.ALL)
                CascadeType.ALL 옵션으로 Book 최상위 엔티티 아래로 자식 카테고리들은 자동추가되게 설정
            */

            return categoryRepository.save(category)
                    .getCode();
        }


    }

}
