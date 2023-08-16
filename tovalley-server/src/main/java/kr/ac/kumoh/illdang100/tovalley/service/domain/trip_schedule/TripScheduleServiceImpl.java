package kr.ac.kumoh.illdang100.tovalley.service.domain.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripScheduleServiceImpl implements TripScheduleService {

    private final TripScheduleRepository tripScheduleRepository;

    @Override
    public TripSchedule addTripSchedule(Long memberId, Long waterPlaceId) {
        return null;
    }

    @Override
    public void deleteTripSchedule(Long memberId, Long scheduleId) {

    }


    /**
     * @methodnme: getTripAttendeesByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
     * @param yearMonth: 클라이언트로부터 요청받은 날짜 정보
     * @description: 물놀이 장소 여행객 수 조회
     * @return: 요청한 달의 여행객 수
     */
    @Override
    public Map<LocalDate, Integer> getTripAttendeesByWaterPlace(Long waterPlaceId, YearMonth yearMonth) {
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
}
