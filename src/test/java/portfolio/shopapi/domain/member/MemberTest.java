package portfolio.shopapi.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.domain.embedded.Address;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager entityManager;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 회원 등록 및 검증
     */
    @Test
    public void createMemberTest() {

        Member member = Member.builder()
                            .address(new Address())
                            .name("테스트")
                            .phone("01071656293")
                            .build();

        entityManager.persist(member);

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.id.eq(member.getId()))
                .fetchOne();

        Assertions.assertThat(findMember).isEqualTo(member);
    }

}