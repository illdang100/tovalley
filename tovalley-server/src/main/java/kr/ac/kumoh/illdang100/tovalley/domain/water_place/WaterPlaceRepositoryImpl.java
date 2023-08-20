package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.ac.kumoh.illdang100.tovalley.domain.CityEnum;
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
    public Page<RetrieveWaterPlacesDto> findWaterPlaceList(RetrieveWaterPlacesCondition condition, Pageable pageable) {
        String province = condition.getProvince();
        CityEnum city = condition.getCity();
        String searchWord = condition.getSearchWord();

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
                .from(waterPlace)
                .where(provinceEq(province),
                        cityEq(city),
                        searchWordContain(searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        addSorting(query, pageable.getSort());

        List<RetrieveWaterPlacesDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(waterPlace.count())
                .from(waterPlace)
                .where(provinceEq(province),
                        cityEq(city),
                        searchWordContain(searchWord));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression provinceEq(String province) {
        return province.equals("전국") ? null : waterPlace.province.eq(province);
    }

    private BooleanExpression cityEq(CityEnum city) {
        return (city == null) ? null : waterPlace.city.eq(city.toString());
    }

    private BooleanExpression searchWordContain (String searchWord) {
        return (searchWord == null) ? null : waterPlace.waterPlaceName.containsIgnoreCase(searchWord);
    }


    private void addSorting(JPAQuery<?> query, Sort sort) {
        PathBuilder<?> pathBuilder = new PathBuilder<>(waterPlace.getType(), waterPlace.getMetadata());

        OrderSpecifier<Double> ratingOrder = pathBuilder.getNumber("rating", Double.class).desc();
        OrderSpecifier<Long> reviewCountOrder = pathBuilder.getNumber("reviewCount", Long.class).desc();
        OrderSpecifier<Long> idOrder = pathBuilder.getNumber("id", Long.class).asc();

        for (Sort.Order o : sort) {
            String property = o.getProperty();
            OrderSpecifier<?> orderSpecifier;
            if ("reviewCount".equals(property)) {
                orderSpecifier = new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.getNumber(property, Long.class));
                query.orderBy(orderSpecifier, ratingOrder, idOrder);
            } else if ("rating".equals(property)) {
                orderSpecifier = new OrderSpecifier<>(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.getNumber(property, Double.class));
                query.orderBy(orderSpecifier, reviewCountOrder, idOrder);
            } else {
                continue;
            }
        }
    }
}