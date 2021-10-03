package portfolio.shopapi.entity.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.embedded.Address;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.member.MemberResponse;
import portfolio.shopapi.service.MemberService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 회원 등록 및 검증
     */
    @Test
    public void createMemberTest() {

        Member member = Member.builder()
                .name("테스트")
                .address(
                        new Address("서울", "강서구", "12")
                )
                .phone("01071656293")
                .build();


        // Spring Jpa Data 기본 메서드 (SimpleJpaRepository.save)
        Member saveMember = memberRepository.save(member);

        // QueryDsl Custom 클래스 메서드 (MemberRepositoryImpl.findMemberById)
        Member findMember = memberRepository.findMemberById(saveMember.getId());

        Assertions.assertThat(saveMember.getId()).isEqualTo(findMember.getId());


    }

}