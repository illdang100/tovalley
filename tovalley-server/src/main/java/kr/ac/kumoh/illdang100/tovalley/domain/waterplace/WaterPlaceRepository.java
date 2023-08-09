package kr.ac.kumoh.illdang100.tovalley.domain.waterplace;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterPlaceRepository extends JpaRepository<WaterPlace, Long> {

    boolean existsByWaterPlaceName(String waterPlaceName);
}