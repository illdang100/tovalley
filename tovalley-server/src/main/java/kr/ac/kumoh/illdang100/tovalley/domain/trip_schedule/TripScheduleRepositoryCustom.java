package kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;

public interface TripScheduleRepositoryCustom {

    Slice<MyTripScheduleRespDto> findSliceMyTripSchedulesByMemberId(Long memberId, Pageable pageable, LocalDate date);
}
