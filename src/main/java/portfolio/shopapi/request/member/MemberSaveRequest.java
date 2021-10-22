package portfolio.shopapi.request.member;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberSaveRequest {

    @NotEmpty(message = "회원 이름값이 불명확합니다.")
    private String name;

    @NotEmpty(message = "회원 주소값이 불명확합니다.")
    private String city;

    @NotEmpty(message = "회원 주소값이 불명확합니다.")
    private String street;

    @NotEmpty(message = "회원 주소값이 불명확합니다.")
    private String zipcode;

    @NotEmpty(message = "회원 휴대폰번호값이 불명확합니다.")
    private String phone;

    @Builder
    public MemberSaveRequest(String name, String city, String street, String zipcode, String phone) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.phone = phone;
    }
}
