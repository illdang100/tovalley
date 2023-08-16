package kr.ac.kumoh.illdang100.tovalley.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findAllByWaterPlace_Id(Long waterPlaceId);
}
