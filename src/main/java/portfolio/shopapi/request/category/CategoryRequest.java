package portfolio.shopapi.request.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryRequest {

    @NotNull(message = "카테고리는 필수입니다.")
    private CategorySet category;

    private List<CategorySet> child_category = new ArrayList<>();

    @Builder
    public CategoryRequest(CategorySet category, List<CategorySet> child_category) {
        this.category = category;
        this.child_category = child_category;
    }
}
