package portfolio.shopapi.entity.Category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.repository.category.CategoryRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(value = false)
    public void relationTest() {

        Category category_Book = new Category("Book", "도서");
        Category save = categoryRepository.save(category_Book);

        Category category_Autobiography = new Category("autobiography", "자서전");
        Category save1 = categoryRepository.save(category_Autobiography);

        Category category_Major = new Category("major", "전공");
        Category save2 = categoryRepository.save(category_Major);

        save.addCategory(save1);
        save.addCategory(save2);

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

}