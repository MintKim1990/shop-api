package portfolio.shopapi.repository.category;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.dto.condition.CategoryCondition;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.category.QCategory;
import portfolio.shopapi.response.category.CategoryCountListResponse;
import portfolio.shopapi.response.category.CategoryListResponse;

import java.util.List;

import static portfolio.shopapi.entity.category.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Book 카테고리 아래로 등록된 자식 카테고리 리스트 추출
     */
    @Override
    public List<CategoryListResponse> findCategoryList(CategoryCondition condition) {

        // 셀프조인시 Q타입 클래스에 variable 메타데이터가 중복되므로 새로 값을 주어 생성
        QCategory categorySub = new QCategory("childCategory");

        return queryFactory
                .select(
                        // CategoryListResponse 객체에 생성자로 데이터 삽입
                        Projections.constructor(
                                CategoryListResponse.class,
                                categorySub.code,
                                categorySub.name
                        )
                )
                .from(category)
                .leftJoin(categorySub)
                .on(category.code.eq(categorySub.category_parent.code)) // PK 조인이 아닌 일반컬럼 조인필요시 on절 사용
                .where(category.code.eq(condition.getCode()))
                .fetch();

    }

    /**
     * 카테고리별 자식카테고리 보유 건수
     */
    @Override
    public List<CategoryCountListResponse> findReliationCountCategory() {

        // 서브쿼리시 Q타입 클래스에 variable 메타데이터가 중복되므로 새로 값을 주어 생성
        QCategory categorySub = new QCategory("childCategory");

        return queryFactory
                .select(
                        // CategoryCountListResponse 객체에 생성자로 데이터 삽입
                        Projections.constructor(
                                CategoryCountListResponse.class,
                                category.code,
                                // as 문법
                                ExpressionUtils.as(
                                        // 서브쿼리
                                        JPAExpressions.select(
                                                categorySub.code.count()
                                        )
                                                .from(categorySub)
                                                .where(categorySub.category_parent.code.eq(category.code))
                                        , "count")
                        )
                )
                .from(category)
                .fetch();

    }


}
