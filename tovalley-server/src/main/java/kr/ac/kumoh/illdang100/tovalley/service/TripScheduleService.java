package kr.ac.kumoh.illdang100.tovalley.service;

import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;

import java.time.LocalDate;
import java.util.Map;

/**
 * 사용자 여행 일정 관리
 */
public interface TripScheduleService {

    // 여행 일정 추가
    TripSchedule addTripSchedule(Long memberId, Long waterPlaceId);

    // 여행 일정 삭제
    void deleteTripSchedule(Long memberId, Long scheduleId);

    // 특정 회원의 앞으로의 일정 조회
    List<TripScheduleDetailRespDto> getUpcomingTripSchedules(Long memberId);

    // 특정 회원의 지난 일정 조회
    List<TripScheduleDetailRespDto> getPastTripSchedulesByMemberId(Long memberId);

    // 날짜별 물놀이 장소 여행자 수 (Map<LocalDate, Integer>)
    List<WaterPlaceVisitorCntRespDto> getTravelAttendeesByWaterPlaceId(String waterPlaceId)
}
