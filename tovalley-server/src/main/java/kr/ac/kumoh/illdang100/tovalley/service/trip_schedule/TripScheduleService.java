package kr.ac.kumoh.illdang100.tovalley.service.trip_schedule;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleRespDto.*;

/**
 * 사용자 여행 일정 관리
 */
public interface TripScheduleService {

    // 여행 일정 추가
    Map<LocalDate, Integer> addTripSchedule(Long memberId, AddTripScheduleReqDto addTripScheduleReqDto);

    // 여행 일정 삭제
    void deleteTripSchedules(Long memberId, List<Long> tripScheduleIds);

    // 특정 회원의 앞으로의 일정 조회
    List<MyTripScheduleRespDto> getUpcomingTripSchedules(Long memberId);

    // 특정 회원의 지난 일정 조회
    Slice<MyTripScheduleRespDto> getPreTripSchedulesByMemberId(Long memberId, Pageable pageable);

    // 날짜별 물놀이 장소 여행자 수 (Map<LocalDate, Integer>)
    Map<LocalDate, Integer> getTripAttendeesByWaterPlace(Long waterPlaceId, YearMonth yearMonth);
}
