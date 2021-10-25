package portfolio.shopapi.request.category;

import lombok.Builder;
import lombok.Data;

@Data
public class CategorySet {

    private String code;
    private String name;

    @Builder
    public CategorySet(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
