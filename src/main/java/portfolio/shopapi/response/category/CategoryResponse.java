package portfolio.shopapi.response.category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.response.item.book.BookResponse;

@Data
@NoArgsConstructor
public class CategoryResponse {

    private String code;
    private String name;

    @Builder
    private CategoryResponse(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CategoryResponse createCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

}
