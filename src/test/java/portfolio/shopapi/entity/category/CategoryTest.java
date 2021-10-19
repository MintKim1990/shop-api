package portfolio.shopapi.entity.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.dto.condition.CategoryCondition;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.response.category.CategoryCountListResponse;
import portfolio.shopapi.response.category.CategoryListResponse;

import java.util.List;

@SpringBootTest
class CategoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * 카테고리 부모 자식관계 등록
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void relationTest() {

        Category category_Book = new Category("Book", "도서");
        Category book = categoryRepository.save(category_Book);

        Category category_Autobiography = new Category("Autobiography", "자서전");
        Category Autobiography = categoryRepository.save(category_Autobiography);

        Category category_Major = new Category("Major", "전공");
        Category Major = categoryRepository.save(category_Major);

        book.addChildCategory(Autobiography);
        book.addChildCategory(Major);

        /*
           SimpleJpaRepository.save 메소드는 자체적으로 @Transactional 어노테이션을 사용하고있으나
           Test 메소드 내에 @Transactional 어노테이션을 사용하므로
           save 메소드 내에 트랜잭션은 Test 메소드 내에 트랜잭션으로 포함된다.

           따라서 save 메소드를 호출해도 바로 Insert가 되지않으며
           save.addCategory(save1) 호출로 변경감지를 사욜해도 바로 Update가 되지않으며

           categoryRepository.findAll() 메소드를 호출할때 Flush가 일어나는데
           findAll 내부를 찾아보면 JPQL을 사용하고있어서 JPQL 실행전에 Flush가 일어나고 JPQL이 실행되어
           findAll 메소드를 호출할때 Insert, Update가 일어난다.

           Flush가 일어나는 조건
           1. EntityManager.flush() 강제호출
           2. 트랜잭션 커밋 시 플러시 자동 호출
           3. JPQL 쿼리 실행 시 플러시 자동 호출
         */

        // 내부적으로 JPQL 사용
        List<Category> categories = categoryRepository.findAll();

    }

    /**
     * Book 카테고리 아래로 등록된 자식 카테고리 리스트 추출
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void searchCategoryChildListTest() {

        Category Book = new Category("Book", "도서");

        Category Autobiography = new Category("Autobiography", "자서전");

        Category Major = new Category("Major", "전공");

        Category Electric = new Category("Electric", "전기과");

        Book.addChildCategory(Autobiography);
        Book.addChildCategory(Major);

        Major.addChildCategory(Electric);

        /*
            자식 카테고리들을 저장하는 로직이 존재하지 않는이유
            @OneToMany(mappedBy = "category_parent", cascade = CascadeType.ALL)
            CascadeType.ALL 옵션으로 Book 최상위 엔티티 아래로 자식 카테고리들은 자동추가되게 설정
         */
        categoryRepository.save(Book);

        CategoryCondition condition = CategoryCondition.builder()
                .code("Book")
                .build();

        List<CategoryListResponse> categoryList = categoryRepository.findCategoryList(condition);

        for (CategoryListResponse categoryListResponse : categoryList) {
            System.out.println("categoryListResponse = " + categoryListResponse);
        }
    }

    /**
     * 카테고리별 자식카테고리 보유 건수
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void findReliationCountCategoryTest() {

        Category category_Book = new Category("Book", "도서");
        Category book = categoryRepository.save(category_Book);

        Category category_Autobiography = new Category("Autobiography", "자서전");
        Category Autobiography = categoryRepository.save(category_Autobiography);

        Category category_Major = new Category("Major", "전공");
        Category Major = categoryRepository.save(category_Major);

        Category category_Electric = new Category("Electric", "전기과");
        Category Electric = categoryRepository.save(category_Electric);

        book.addChildCategory(Autobiography);
        book.addChildCategory(Major);

        Major.addChildCategory(Electric);

        List<CategoryCountListResponse> reliationCountCategory = categoryRepository.findReliationCountCategory();

        for (CategoryCountListResponse categoryCountListResponse : reliationCountCategory) {
            System.out.println("categoryCountListResponse = " + categoryCountListResponse);
        }
    }



}