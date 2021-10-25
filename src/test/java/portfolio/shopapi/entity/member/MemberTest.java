package portfolio.shopapi.entity.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.service.member.MemberService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    MemberService memberService;

    /**
     * 회원 등록 및 검증
     */
    @Test
    public void createMemberTest() {

        Long saveMemberId = memberService.saveMember(
                new MemberSaveRequest(
                        "테스트",
                        "서울",
                        "강서구",
                        "123",
                        "01071656293"
                )
        );


        // QueryDsl Custom 클래스 메서드 (MemberRepositoryImpl.findMemberById)
        Member findMember = memberService.findMemberById(saveMemberId);

        Assertions.assertThat(saveMemberId).isEqualTo(findMember.getId());

    }

}