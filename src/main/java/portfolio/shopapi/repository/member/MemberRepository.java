package portfolio.shopapi.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.shopapi.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
