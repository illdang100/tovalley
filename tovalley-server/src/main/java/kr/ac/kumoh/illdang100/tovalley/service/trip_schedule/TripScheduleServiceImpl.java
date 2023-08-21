package kr.ac.kumoh.illdang100.tovalley.service.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule.TripScheduleReqDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripScheduleServiceImpl implements TripScheduleService {

    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final WaterPlaceRepository waterPlaceRepository;

    private static final int MAX_TRIP_SCHEDULES = 5;

    @Override
    @Transactional
    public Map<LocalDate, Integer> addTripSchedule(Long memberId, AddTripScheduleReqDto addTripScheduleReqDto) {
        Member findMember = findMemberOrElseThrowEx(memberId);
        Long waterPlaceId = addTripScheduleReqDto.getWaterPlaceId();
        WaterPlace findWaterPlace = findWaterPlaceByWaterPlaceIdOrElseThrowEx(waterPlaceId);

        LocalDate now = LocalDate.now();
        LocalDate tripDate = addTripScheduleReqDto.getTripDate();

        validateTripDate(tripDate, now);

        validateMaxTripSchedules(memberId);

        validateDuplicateTripSchedule(memberId, waterPlaceId, tripDate);

        tripScheduleRepository.save(TripSchedule.builder()
                .member(findMember)
                .waterPlace(findWaterPlace)
                .tripDate(tripDate)
                .tripNumber(addTripScheduleReqDto.getTripPartySize())
                .build());

        return getTripAttendeesByWaterPlace(waterPlaceId, YearMonth.from(tripDate));
    }

    private void validateTripDate(LocalDate tripDate, LocalDate now) {
        if (tripDate.isBefore(now)) {
            throw new CustomApiException("현재 날짜(" + now + ") 이전에는 여행 일정을 추가할 수 없습니다");
        }
    }

    private void validateMaxTripSchedules(Long memberId) {
        int upcomingTripSchedulesCnt = tripScheduleRepository.countByMemberIdAndTripDateGreaterThanEqual(memberId, LocalDate.now());
        if (upcomingTripSchedulesCnt >= MAX_TRIP_SCHEDULES) {
            throw new CustomApiException("여행 일정 추가는 최대 " + MAX_TRIP_SCHEDULES +"개까지 가능합니다");
        }
    }

    private void validateDuplicateTripSchedule(Long memberId, Long waterPlaceId, LocalDate tripDate) {
        if (tripScheduleRepository.existsByMember_IdAndWaterPlace_IdAndTripDate(memberId, waterPlaceId, tripDate)) {
            throw new CustomApiException("이미 존재하는 여행 일정입니다");
        }
    }

    @Override
    public void deleteTripSchedule(Long memberId, Long scheduleId) {

    }


    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @param yearMonth: 클라이언트로부터 요청받은 날짜 정보
     * @methodnme: getTripAttendeesByWaterPlace
     * @author: JYeonJun
     * @description: 물놀이 장소 여행객 수 조회
     * @return: 요청한 달의 여행객 수
     */
    @Override
    public Map<LocalDate, Integer> getTripAttendeesByWaterPlace(Long waterPlaceId, YearMonth yearMonth) {

        findWaterPlaceByWaterPlaceIdOrElseThrowEx(waterPlaceId);

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<TripSchedule> findTripSchedules =
                tripScheduleRepository.findByWaterPlaceIdAndTripDateBetween(waterPlaceId, firstDayOfMonth, lastDayOfMonth);

        Map<LocalDate, Integer> attendeesByDate = new HashMap<>();
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            LocalDate tmpDate = date;
            int attendees = findTripSchedules.stream()
                    .filter(tripSchedule -> tripSchedule.getTripDate().equals(tmpDate))
                    .mapToInt(TripSchedule::getTripNumber)
                    .sum();
            attendeesByDate.put(date, attendees);
        }

        return attendeesByDate;
    }

    private Member findMemberOrElseThrowEx(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다."));
    }

    private WaterPlace findWaterPlaceByWaterPlaceIdOrElseThrowEx(Long waterPlaceId) {
        WaterPlace findWaterPlace = waterPlaceRepository.findById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
        return findWaterPlace;
    }
}
