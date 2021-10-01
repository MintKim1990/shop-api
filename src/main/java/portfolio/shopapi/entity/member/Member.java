package portfolio.shopapi.entity.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.request.member.MemberDto;
import portfolio.shopapi.entity.embedded.Address;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Column(length = 11)
    private String phone;

    @Builder
    public Member(Long id, String name, Address address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public static Member CreateMember(MemberDto memberDto) {
        return Member.builder()
                .name(memberDto.getName())
                .address(new Address(
                        memberDto.getCity(),
                        memberDto.getStreet(),
                        memberDto.getZipcode()
                ))
                .phone(memberDto.getPhone())
                .build();
    }

}
