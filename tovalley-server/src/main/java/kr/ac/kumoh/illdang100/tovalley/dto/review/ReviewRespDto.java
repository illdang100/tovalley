package kr.ac.kumoh.illdang100.tovalley.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewRespDto {

    @AllArgsConstructor
    @Data
    public static class WaterPlaceReviewRespDto {

        private Long reviewId;
        private String memberProfileImg;
        private String nickname;
        private Double rating;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdReviewDate;
        private String content;
        private List<String> reviewImages;

        public WaterPlaceReviewRespDto(Long reviewId, String memberProfileImg, String nickname, Double rating, LocalDateTime createdReviewDate, String content) {
            this.reviewId = reviewId;
            this.memberProfileImg = memberProfileImg;
            this.nickname = nickname;
            this.rating = rating;
            this.createdReviewDate = createdReviewDate;
            this.content = content;
        }
    }
}
