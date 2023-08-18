package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class WaterPlaceRespDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesDto {
        private Long waterPlaceId; // 물놀이 장소 pk
        private String waterPlaceName; // 물놀이 장소 이름
        private String town; // town 정보 (null일 수 있음)
        private Double waterPlaceRating; // 평점
        private Integer waterPlaceReviewCnt; // 리뷰수
        private String managementType; // managementType 정보 (일반지역, 중점관리 지역)
        private String waterPlaceCategory; // waterPlaceCategory 정보 (하천, 계곡)
    }
}
