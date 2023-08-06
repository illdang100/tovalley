package kr.ac.kumoh.illdang100.tovalley.service;

import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

/**
 * 리뷰 관리
 */
public interface ReviewService {

    // 리뷰 작성
    // 리뷰 작성 후 리뷰 이미지의 fk(reviewId) 등록하기
    Review writeReview(Long memberId, Long waterPlaceId);

    // 리뷰 이미지 추가
    void addReviewImage(MultipartFile reviewImage);

    // 리뷰 이미지 삭제
    void deleteReviewImage(Long reviewImageId);

    // 특정 회원의 리뷰 조회
    Page<ReviewDetailRespDto> getReviewsByMemberId(Long memberId);

    // 특정 물놀이 장소의 리뷰 조회
    Page<ReviewDetailRespDto> getReviewsByWaterPlaceId(Long waterPlaceId);
}
