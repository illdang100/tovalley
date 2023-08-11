package kr.ac.kumoh.illdang100.tovalley.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select COUNT(r) from Review r where r.waterPlace.id = :wId")
    int findReviewCnt(@Param("wId") Long waterPlaceId);
}
