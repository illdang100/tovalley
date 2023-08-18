package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

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

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.domain.water_place.QWaterPlace.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

public class WaterPlaceRepositoryImpl implements WaterPlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WaterPlaceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RetrieveWaterPlacesDto> findWaterPlaceList(RetrieveWaterPlacesCondition RetrieveWaterPlacesCondition, Pageable pageable) {
        String region = RetrieveWaterPlacesCondition.getRegion();
        String searchWord = RetrieveWaterPlacesCondition.getSearchWord();
        JPAQuery<RetrieveWaterPlacesDto> query = queryFactory
                .select(Projections.constructor(RetrieveWaterPlacesDto.class,
                        waterPlace.id,
                        waterPlace.waterPlaceName,
                        waterPlace.town,
                        waterPlace.rating,
                        waterPlace.reviewCount,
                        waterPlace.managementType,
                        waterPlace.waterPlaceCategory
                ))
                .from(waterPlace);

        BooleanExpression regionPredicate;

        if (region.equals("전국")) {
            regionPredicate = waterPlace.id.isNotNull();
        } else {
            regionPredicate = waterPlace.province.eq(region);
        }

        if (searchWord != null && !searchWord.isEmpty()) {
            regionPredicate = regionPredicate.and(waterPlace.waterPlaceName.containsIgnoreCase(searchWord));
        }

        query = query.where(regionPredicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    waterPlace.getType(), waterPlace.getMetadata()
            );

            String property = o.getProperty();
            if (property.equals("rating")) {
                query.orderBy(
                        new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.getNumber(property, Double.class)),
                        pathBuilder.getNumber("reviewCount", Long.class).desc(),
                        pathBuilder.getNumber("id", Long.class).asc()
                );
            } else if (property.equals("reviewCount")) {
                query.orderBy(
                        new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.getNumber(property, Long.class)),
                        pathBuilder.getNumber("rating", Double.class).desc(),
                        pathBuilder.getNumber("id", Long.class).asc()
                );
            }
        }

        List<RetrieveWaterPlacesDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(waterPlace.count())
                .from(waterPlace);

        if (region.equals("전국")) {
            countQuery.where(waterPlace.id.isNotNull());
        } else {
            countQuery.where(waterPlace.province.eq(region));
        }

        if (searchWord != null && !searchWord.isEmpty()) {
            countQuery.where(waterPlace.waterPlaceName.containsIgnoreCase(searchWord));
        }

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
