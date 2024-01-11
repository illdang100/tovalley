package kr.ac.kumoh.illdang100.tovalley.domain.member;

import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.member;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleRespDto.SearchMembersRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<SearchMembersRespDto> findSliceMembersByNickname(String nickname, Pageable pageable) {

        JPAQuery<SearchMembersRespDto> query = queryFactory
                .select(Projections.constructor(SearchMembersRespDto.class,
                        member.id,
                        member.email,
                        member.memberName,
                        member.nickname,
                        member.role))
                .from(member)
                .where(searchNicknameEquals(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    member.getType(), member.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<SearchMembersRespDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression searchNicknameEquals(String nickname) {
        return (nickname == null) ? null : member.nickname.eq(nickname);
    }
}
