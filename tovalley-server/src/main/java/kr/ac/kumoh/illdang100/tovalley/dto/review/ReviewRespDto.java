package kr.ac.kumoh.illdang100.tovalley.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewRespDto {

    @AllArgsConstructor
    @Data
    public static class WaterPlaceReviewRespDto {

        private Long reviewId;
        private String memberProfileImg;
        private String nickname;
        private Integer rating;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdReviewDate;
        private String content;
        private List<String> reviewImages;

        public WaterPlaceReviewRespDto(Long reviewId, String memberProfileImg, String nickname, Integer rating, LocalDateTime createdReviewDate, String content) {
            this.reviewId = reviewId;
            this.memberProfileImg = memberProfileImg;
            this.nickname = nickname;
            this.rating = rating;
            this.createdReviewDate = createdReviewDate;
            this.content = content;
        }
    }

    @AllArgsConstructor
    @Data
    public static class MyReviewRespDto {

        private Long reviewId;
        private Integer rating;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdReviewDate;
        private String content;
        private List<String> reviewImages;
    }

    @AllArgsConstructor
    @Data
    public static class WaterPlaceReviewDetailRespDto {
        private Double waterPlaceRating; // 물놀이 장소 평점
        private int reviewCnt; // 리뷰 수
        private Map<Integer, Long> ratingRatio;
        private Page<ReviewRespDto.WaterPlaceReviewRespDto> reviews; // 리뷰
    }
}
