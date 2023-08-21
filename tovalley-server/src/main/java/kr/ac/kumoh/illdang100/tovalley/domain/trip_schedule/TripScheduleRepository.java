package kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {

    List<TripSchedule> findByWaterPlaceIdAndTripDateBetween(Long waterPlaceId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth);

    int countByMemberIdAndTripDateGreaterThanEqual(Long memberId, LocalDate today);

    boolean existsByMember_IdAndWaterPlace_IdAndTripDate(Long memberId, Long waterPlaceId, LocalDate tripDate);
}
