package kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.member;
import static kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.QTripSchedule.tripSchedule;
import static kr.ac.kumoh.illdang100.tovalley.domain.water_place.QWaterPlace.waterPlace;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;

public class TripScheduleRepositoryImpl implements TripScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TripScheduleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<MyTripScheduleRespDto> findSliceMyTripSchedulesByMemberId(Long memberId, Pageable pageable, LocalDate date) {

        JPAQuery<MyTripScheduleRespDto> query = queryFactory
                .select(Projections.constructor(MyTripScheduleRespDto.class,
                        tripSchedule.id,
                        waterPlace.id,
                        waterPlace.waterPlaceName,
                        waterPlace.waterPlaceImage,
                        waterPlace.address,
                        waterPlace.rating,
                        waterPlace.reviewCount,
                        tripSchedule.tripDate,
                        tripSchedule.tripNumber
                ))
                .from(tripSchedule)
                .join(tripSchedule.waterPlace, waterPlace)
                .join(tripSchedule.member, member)
                .where(member.id.eq(memberId))
                .where(tripSchedule.tripDate.before(date))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    tripSchedule.getType(), tripSchedule.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<MyTripScheduleRespDto> result = query.fetch();

        boolean hasNext = false;
        if (result. size() > pageable. getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
