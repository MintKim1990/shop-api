package portfolio.shopapi.repository.item;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.response.category.CategoryListResponse;
import portfolio.shopapi.response.item.book.BookResponse;

import java.util.List;

import static portfolio.shopapi.entity.item.QItem.item;
import static portfolio.shopapi.entity.item.book.QBook.book;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<BookResponse> findAutobiography() {

        return queryFactory
                .select(
                        // CategoryListResponse 객체에 생성자로 데이터 삽입
                        Projections.constructor(
                                BookResponse.class,
                                item.id,
                                item.name,
                                item.price,
                                item.stockQuantity,
                                book.auther,
                                book.isbn
                        )
                )
                .from(item)
                .join(book)
                .on(item.id.eq(book.id))
                .fetch();

    }

}
