package kr.ac.kumoh.illdang100.tovalley.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

public interface ReviewRepositoryCustom {

    Page<WaterPlaceReviewRespDto> findReviewsByWaterPlaceId(Long waterPlaceId, Pageable pageable);
}
