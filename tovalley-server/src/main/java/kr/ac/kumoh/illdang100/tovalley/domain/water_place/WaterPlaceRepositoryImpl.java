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
                        waterPlace.address,
                        waterPlace.rating,
                        waterPlace.reviewCount,
                        waterPlace.managementType,
                        waterPlace.waterPlaceCategory,
                        waterPlace.waterPlaceImage.storeFileUrl
                ))
                .from(waterPlace)
                .where(provinceEq(province),
                        cityEq(city),
                        searchWordContain(searchWord))
                .orderBy(addSorting(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<RetrieveWaterPlacesDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(waterPlace.count())
                .from(waterPlace)
                .where(provinceEq(province),
                        cityEq(city),
                        searchWordContain(searchWord));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<AdminRetrieveWaterPlacesDto> getAdminWaterPlaceList(String searchWord, Pageable pageable) {
        List<AdminRetrieveWaterPlacesDto> content = queryFactory
                .select(Projections.constructor(AdminRetrieveWaterPlacesDto.class,
                        waterPlace.id,
                        waterPlace.waterPlaceImage.storeFileUrl,
                        waterPlace.waterPlaceName,
                        waterPlace.province,
                        waterPlace.city
                ))
                .from(waterPlace)
                .where(searchWordContain(searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(waterPlace.count())
                .from(waterPlace)
                .where(searchWordContain(searchWord));

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


    private OrderSpecifier<?> addSorting(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "rating":
                        return new OrderSpecifier<>(direction, waterPlace.rating);
                    case "review":
                        return new OrderSpecifier<>(direction, waterPlace.reviewCount);
                }
            }
        }
        return null;
    }
}