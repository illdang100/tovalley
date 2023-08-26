package kr.ac.kumoh.illdang100.tovalley.dto.trip_schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.text.DecimalFormat;
import java.time.LocalDate;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;

public class TripScheduleRespDto {

    @Getter
    public static class MyTripScheduleRespDto {

        private Long tripScheduleId; // 여행 일정 Id(PK)
        private Long waterPlaceId;    // 물놀이 장소 Id(PK)
        private String waterPlaceName; // 물놀이 장소명
        private String waterPlaceImg; // 물놀이 장소 이미지
        private String waterPlaceAddr; // 물놀이 장소 주소
        private String waterPlaceRating; // 물놀이 장소 평점
        private int waterPlaceReviewCnt; // 물놀이 장소 리뷰슈
        private int waterPlaceTraffic; // 물놀이 장소 혼잡도
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate tripDate; // 여행 날짜
        private int tripPartySize; // 여행 인원수
        private RescueSupplyByWaterPlaceRespDto rescueSupplies; // 구급용품 현황
        private Boolean hasReview; // 리뷰 작성 여부

        public MyTripScheduleRespDto(Long tripScheduleId, Long waterPlaceId, String waterPlaceName,
                                     String waterPlaceImg, String waterPlaceAddr, Double waterPlaceRating,
                                     int waterPlaceReviewCnt, LocalDate tripDate, int tripPartySize) {
            this.tripScheduleId = tripScheduleId;
            this.waterPlaceId = waterPlaceId;
            this.waterPlaceName = waterPlaceName;
            this.waterPlaceImg = waterPlaceImg;
            this.waterPlaceAddr = waterPlaceAddr;
            this.waterPlaceRating = getFormattedRating(waterPlaceRating);
            this.waterPlaceReviewCnt = waterPlaceReviewCnt;
            this.tripDate = tripDate;
            this.tripPartySize = tripPartySize;
        }

        public MyTripScheduleRespDto(Long tripScheduleId, Long waterPlaceId, String waterPlaceName, String waterPlaceImg,
                                     String waterPlaceAddr, Double waterPlaceRating, int waterPlaceReviewCnt,
                                     int waterPlaceTraffic, LocalDate tripDate,
                                     RescueSupplyByWaterPlaceRespDto rescueSupplies, int tripPartySize,
                                     Boolean hasReview) {
            this.tripScheduleId = tripScheduleId;
            this.waterPlaceId = waterPlaceId;
            this.waterPlaceName = waterPlaceName;
            this.waterPlaceImg = waterPlaceImg;
            this.waterPlaceAddr = waterPlaceAddr;
            this.waterPlaceRating = getFormattedRating(waterPlaceRating);
            this.waterPlaceReviewCnt = waterPlaceReviewCnt;
            this.waterPlaceTraffic = waterPlaceTraffic;
            this.tripDate = tripDate;
            this.rescueSupplies = rescueSupplies;
            this.tripPartySize = tripPartySize;
            this.hasReview = hasReview;
        }

        private String getFormattedRating(Double rating) {
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            return decimalFormat.format(rating);
        }

        public void changeWaterPlaceTraffic(int waterPlaceTraffic) {
            this.waterPlaceTraffic = waterPlaceTraffic;
        }

        public void changeRescueSupplies(RescueSupplyByWaterPlaceRespDto rescueSupplies) {
            this.rescueSupplies = rescueSupplies;
        }

        public void changeHasReview(Boolean hasReview) {
            this.hasReview = hasReview;
        }
    }
}
