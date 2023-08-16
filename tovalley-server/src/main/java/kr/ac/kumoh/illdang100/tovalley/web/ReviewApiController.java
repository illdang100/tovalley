package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.domain.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.WaterPlaceDetailPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Review", description = "리뷰 API Document")
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping("/auth/water-places/{id}/reviews")
    @Operation(summary = "물놀이 장소별 리뷰 조회", description = "물놀이 장소별 리뷰 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceReviews(@PathVariable("id") Long waterPlaceId,
                                                  @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WaterPlaceReviewDetailRespDto result = reviewService.getReviewsByWaterPlaceId(waterPlaceId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소의 리뷰 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/auth/members/reviews")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자가 작성한 리뷰 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceReviews(/*@PathVariable("id") Long memberId,*/
                                                  @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WaterPlaceReviewRespDto> result =
                reviewService.getReviewsByMemberId(0L);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 리뷰 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
