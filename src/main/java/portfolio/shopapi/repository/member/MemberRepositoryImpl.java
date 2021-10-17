package portfolio.shopapi.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.member.Member;

import static portfolio.shopapi.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findMemberById(Long id) {
        return queryFactory
                .selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }
}
