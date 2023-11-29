package kr.ac.kumoh.illdang100.tovalley.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Review", description = "리뷰 API Document")
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사용자 리뷰 작성", description = "사용자가 작성한 리뷰 정보를 데이터베이스에 반영합니다.")
    public ResponseEntity<?> addReviews(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @ModelAttribute @Valid AddNewReviewReqDto addNewReviewReqDto,
                                        BindingResult bindingResult) {

        reviewService.writeReview(principalDetails.getMember().getId(), addNewReviewReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "리뷰가 정상적으로 생성되었습니다", null), HttpStatus.CREATED);
    }

    @GetMapping("/water-places/{id}/reviews")
    @Operation(summary = "물놀이 장소별 리뷰 조회", description = "물놀이 장소별 리뷰 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceReviews(@PathVariable("id") Long waterPlaceId,
                                                  @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WaterPlaceReviewDetailRespDto result = reviewService.getReviewsByWaterPlaceId(waterPlaceId, pageable);  

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소의 리뷰 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/members/reviews")
    @Operation(summary = "사용자별 리뷰 조회", description = "사용자가 작성한 리뷰 정보를 반환합니다.")
    public ResponseEntity<?> getMyReviews(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<MyReviewRespDto> result =
                reviewService.getReviewsByMemberId(principalDetails.getMember().getId(), pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 리뷰 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
