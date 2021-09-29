package portfolio.shopapi.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.domain.embedded.Address;

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
    public Member(String name, Address address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

}
