package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaterPlaceDetailRepository extends JpaRepository<WaterPlaceDetail, Long> {

    @EntityGraph(attributePaths = "waterPlace")
    Optional<WaterPlaceDetail> findWaterPlaceDetailWithWaterPlaceById(Long waterPlaceId);
}
