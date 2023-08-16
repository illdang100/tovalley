package kr.ac.kumoh.illdang100.tovalley.service.domain.review;

import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.WaterPlaceDetailPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review writeReview(Long memberId, Long waterPlaceId) {
        return null;
    }

    @Override
    public void addReviewImage(MultipartFile reviewImage) {

    }

    @Override
    public void deleteReviewImage(Long reviewImageId) {

    }

    @Override
    public Page<WaterPlaceReviewRespDto> getReviewsByMemberId(Long memberId) {
        return null;
    }

    @Override
    public WaterPlaceReviewDetailRespDto getReviewsByWaterPlaceId(Long waterPlaceId,
                                                                  Pageable pageable) {

        List<Review> allReviews = reviewRepository.findAllByWaterPlaceId(waterPlaceId);

        Map<Integer, Long> ratingRatioMap = allReviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        Page<WaterPlaceReviewRespDto> reviewsByWaterPlaceId = reviewRepository.findReviewsByWaterPlaceId(waterPlaceId, pageable);

        return new WaterPlaceReviewDetailRespDto(ratingRatioMap, reviewsByWaterPlaceId);
    }
}
