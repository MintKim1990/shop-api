package portfolio.shopapi.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.constant.ResponseType;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.category.CategoryResponse;
import portfolio.shopapi.response.member.MemberResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 저장
     * @param memberSaveRequest
     * @return
     */
    public Response saveMember(MemberSaveRequest memberSaveRequest) {

        Member member = memberRepository.save(
                Member.CreateMember(memberSaveRequest)
        );

        return new Response<MemberResponse>(
                ResponseType.SUCCESS,
                MemberResponse.createMemberResponse(member)
        );
    }

    /**
     * 회원조회
     * @param id
     * @return
     */
    public Response findMemberById(Long id) {
        return new Response<MemberResponse>(
                ResponseType.SUCCESS,
                MemberResponse.createMemberResponse(
                        memberRepository.findMemberById(id)
                )
        );
    }

    /**
     * 회원 전체조회
     * @return
     */
    public Response findMemberByAll() {
        return new Response<List<MemberResponse>>(
                ResponseType.SUCCESS,
                memberRepository.findAll().stream()
                        .map(member -> MemberResponse.createMemberResponse(member))
                        .collect(Collectors.toList())
        );
    }

}
