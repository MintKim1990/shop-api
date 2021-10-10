package portfolio.shopapi.dto.condition;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryCondition {

    private String code;

    @Builder
    public CategoryCondition(String code) {
        this.code = code;
    }
}
