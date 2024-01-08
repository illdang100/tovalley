package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaterPlaceRepository extends JpaRepository<WaterPlace, Long>, WaterPlaceRepositoryCustom {

    boolean existsByWaterPlaceName(String waterPlaceName);

    @Query("select wp from WaterPlace wp order by wp.rating desc, wp.reviewCount desc")
    List<WaterPlace> findTop8ByOrderByRatingDesc(Pageable pageable);

    @Query("select wp from WaterPlace wp order by wp.reviewCount desc, wp.rating desc")
    List<WaterPlace> findTop8ByOrderByReviewCountDesc(Pageable pageable);
}