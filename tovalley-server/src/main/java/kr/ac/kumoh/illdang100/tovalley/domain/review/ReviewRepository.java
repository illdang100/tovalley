package kr.ac.kumoh.illdang100.tovalley.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    @Query("select r from Review r join r.tripSchedule t where t.waterPlace.id = :wId")
    List<Review> findAllByWaterPlace_Id(@Param("wId") Long waterPlaceId);

    boolean existsByTripScheduleId(Long tripScheduleId);

    @Query("select r from Review r where r.tripSchedule.id in :tripScheduleIds")
    void deleteAllByTripScheduleIds(List<Long> tripScheduleIds);
}
