package portfolio.shopapi.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import portfolio.shopapi.entity.member.Member;
import portfolio.shopapi.entity.member.QMember;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findMemberById(Long id) {
        return queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.id.eq(id))
                .fetchOne();
    }
}
