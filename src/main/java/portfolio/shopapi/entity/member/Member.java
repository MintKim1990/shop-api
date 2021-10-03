package portfolio.shopapi.entity.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.order.Order;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.entity.embedded.Address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Builder
    private Member(Long id, String name, Address address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public static Member CreateMember(MemberSaveRequest memberSaveRequest) {
        return Member.builder()
                .name(memberSaveRequest.getName())
                .address(new Address(
                        memberSaveRequest.getCity(),
                        memberSaveRequest.getStreet(),
                        memberSaveRequest.getZipcode()
                ))
                .phone(memberSaveRequest.getPhone())
                .build();
    }

}
