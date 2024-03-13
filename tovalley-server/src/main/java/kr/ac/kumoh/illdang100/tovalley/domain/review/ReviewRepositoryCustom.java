package kr.ac.kumoh.illdang100.tovalley.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

public interface ReviewRepositoryCustom {

    Page<WaterPlaceReviewRespDto> findReviewsByWaterPlaceId(Long waterPlaceId, Long memberId, Pageable pageable);

    Slice<MyReviewRespDto> findSliceMyReviewByMemberId(Long memberId, Pageable pageable);
}
