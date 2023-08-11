package kr.ac.kumoh.illdang100.tovalley.domain.waterplace;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WaterPlaceRepository extends JpaRepository<WaterPlace, Long> {

    boolean existsByWaterPlaceName(String waterPlaceName);

    @Query("select wp from WaterPlace wp order by wp.rating desc")
    List<WaterPlace> findTop4ByOrderByRatingDesc(Pageable pageable);

    @Query("select wp from WaterPlace wp left join Review  r on wp.id = r.waterPlace.id group by wp.id order by count(r.id) desc")
    List<WaterPlace> findTop4ByOrderByReviewCountDesc(Pageable pageable);
}