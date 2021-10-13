package portfolio.shopapi.repository.item;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.category.QCategory;
import portfolio.shopapi.entity.item.Item;

import portfolio.shopapi.entity.item.book.Autobiography;

import portfolio.shopapi.response.category.CategoryCountListResponse;

import java.util.List;
import static portfolio.shopapi.entity.item.QItem.item;
import static portfolio.shopapi.entity.item.book.QAutobiography.autobiography;
import static portfolio.shopapi.entity.category.QCategory.category;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Item> findAutobiography() {

        return queryFactory
                .select(item)
                .from(item)
                .join(autobiography)
                .on(item.id.eq(autobiography.id))
                .fetch();

    }

}
