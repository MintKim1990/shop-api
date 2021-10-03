package portfolio.shopapi.response.member;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberResponse {

    private long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String phone;

    @Builder
    public MemberResponse(long id, String name, String city, String street, String zipcode, String phone) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.phone = phone;
    }
}
