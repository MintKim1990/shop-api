package portfolio.shopapi.response.category;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryListResponse {

    private String code;
    private String name;

    @QueryProjection
    public CategoryListResponse(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
