package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.domain.accident.QAccident.accident;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;

public class AccidentRepositoryImpl implements AccidentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AccidentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AccidentDetailRespDto> findAccidentDetailRespDtoByWaterPlaceId(Long waterPlaceId,
                                                                               RetrieveAccidentCondition retrieveAccidentCondition,
                                                                               Pageable pageable) {

        JPAQuery<AccidentDetailRespDto> query = queryFactory
                .select(Projections.constructor(AccidentDetailRespDto.class,
                        accident.id,
                        accident.accidentDate,
                        accident.accidentCondition,
                        accident.peopleNum
                ))
                .from(accident)
                .where(accident.waterPlace.id.eq(waterPlaceId),
                        accidentDateEq(retrieveAccidentCondition.getAccidentDate()),
                        accidentCondEq(retrieveAccidentCondition.getAccidentCond()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    accident.getType(), accident.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<AccidentDetailRespDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(accident.count())
                .from(accident)
                .where(accident.waterPlace.id.eq(waterPlaceId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression accidentDateEq(LocalDate accidentDate) {
        return accidentDate != null ? accident.accidentDate.eq(accidentDate) : null;
    }

    private BooleanExpression accidentCondEq(AccidentEnum accidentCond) {
        return accidentCond != null ? accident.accidentCondition.eq(accidentCond) : null;
    }
}
