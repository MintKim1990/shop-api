package portfolio.shopapi.repository.member;

import portfolio.shopapi.entity.member.Member;

public interface MemberRepositoryCustom {
    Member findMemberById(Long id);
}
