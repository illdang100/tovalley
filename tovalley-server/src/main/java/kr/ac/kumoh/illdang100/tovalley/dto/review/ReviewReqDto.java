package kr.ac.kumoh.illdang100.tovalley.dto.review;

import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ReviewReqDto {

    @AllArgsConstructor
    @Data
    public static class AddNewReviewReqDto {

        @NotNull
        private Long tripScheduleId;

        @NotNull
        private WaterQualityReviewEnum waterQuality;

        @NotNull
        @Min(value = 1, message = "평점은 최소 1점 이상입니다")
        @Max(value = 5, message = "평점은 최대 5점 입니다")
        private Integer rating;

        @Size(max = 256)
        private String content;

        @Size(max = 5, message = "사진은 최대 5개까지 추가할 수 있습니다")
        private List<MultipartFile> reviewImages;
    }
}
