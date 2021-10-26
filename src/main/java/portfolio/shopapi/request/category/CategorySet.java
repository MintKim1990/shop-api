package portfolio.shopapi.request.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategorySet {

    @NotEmpty(message = "카테고리코드는 필수입니다.")
    private String code;
    @NotEmpty(message = "카테고리이름은 필수입니다.")
    private String name;

    private String parent_code; // 부모 카테고리 코드

    @Builder
    public CategorySet(String code, String name, String parent_code) {
        this.code = code;
        this.name = name;
        this.parent_code = parent_code;
    }
}
