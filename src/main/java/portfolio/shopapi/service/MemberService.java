package portfolio.shopapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(MemberSaveRequest memberSaveRequest) {
        return memberRepository.save(
                Member.CreateMember(memberSaveRequest)
        ).getId();
    }

    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id);
    }

}
