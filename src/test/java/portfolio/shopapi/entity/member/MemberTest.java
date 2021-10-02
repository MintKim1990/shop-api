package portfolio.shopapi.entity.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.service.MemberService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberService memberService;

    /**
     * 회원 등록 및 검증
     */
    @Test
    public void createMemberTest() {

        MemberSaveRequest memberSaveRequest = new MemberSaveRequest(
                "테스트",
                "서울",
                "강서구",
                "123",
                "01071656293"
        );

        // Spring Jpa Data 기본 메서드 (SimpleJpaRepository.save)
        Long saveId = memberService.save(memberSaveRequest);

        // QueryDsl Custom 클래스 메서드 (MemberRepositoryImpl.findMemberById)
        Member findMember = memberService.findMemberById(saveId);

        Assertions.assertThat(saveId).isEqualTo(findMember.getId());


    }

}