package portfolio.shopapi.entity.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.member.MemberResponse;
import portfolio.shopapi.service.member.MemberService;

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

        Response response = memberService.saveMember(
                new MemberSaveRequest(
                        "테스트",
                        "서울",
                        "강서구",
                        "123",
                        "01071656293"
                )
        );

        MemberResponse memberResponse = (MemberResponse) response.getData();

        Assertions.assertThat(memberResponse.getName()).isEqualTo("테스트");

    }

}