package portfolio.shopapi.response.category;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryCountListResponse {

    private String code;
    private Long count;

    @QueryProjection
    public CategoryCountListResponse(String code, Long count) {
        this.code = code;
        this.count = count;
    }
}
