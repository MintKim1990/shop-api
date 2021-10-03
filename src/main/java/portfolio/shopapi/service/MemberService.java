package portfolio.shopapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.repository.member.MemberRepository;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.member.MemberResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Response save(MemberSaveRequest memberSaveRequest) {

        Member member = memberRepository.save(
                Member.CreateMember(memberSaveRequest)
        );

        return new Response(
                MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .city(member.getAddress().getCity())
                .street(member.getAddress().getStreet())
                .zipcode(member.getAddress().getZipcode())
                .phone(member.getPhone())
                .build()
        );
    }

    public Response findMemberById(Long id) {
        return new Response(
                memberRepository.findMemberById(id)
        );
    }

    public Response findMemberByAll() {
        return new Response(
                memberRepository.findAll()
        );
    }

}
