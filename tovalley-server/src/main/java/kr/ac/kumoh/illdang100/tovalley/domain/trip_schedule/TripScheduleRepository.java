package kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long>, TripScheduleRepositoryCustom {

    List<TripSchedule> findByWaterPlaceIdAndTripDateBetween(Long waterPlaceId, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth);

    @Query("select t from TripSchedule t join fetch t.waterPlace where t.member.id = :mId and t.tripDate >= :date order by t.tripDate")
    List<TripSchedule> findUpcomingWithWaterPlaceByMember(@Param("mId") Long memberId, @Param("date") LocalDate today);

    int countByMemberIdAndTripDateGreaterThanEqual(Long memberId, LocalDate today);

    boolean existsByMember_IdAndWaterPlace_IdAndTripDate(Long memberId, Long waterPlaceId, LocalDate tripDate);

    Optional<TripSchedule> findTripScheduleByIdAndMemberId(Long tripScheduleId, Long memberId);
}
