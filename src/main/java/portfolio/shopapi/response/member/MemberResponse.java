package portfolio.shopapi.response.member;

import lombok.Builder;
import lombok.Data;
import portfolio.shopapi.entity.embedded.Address;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.response.item.book.BookResponse;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberResponse {

    private long id;
    private String name;
    private Address address;
    private String phone;

    @Builder
    private MemberResponse(long id, String name, Address address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public static MemberResponse createMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .phone(member.getPhone())
                .build();
    }

}
