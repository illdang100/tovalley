package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

public class WaterPlaceRespDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesDto {
        private Long waterPlaceId; // 물놀이 장소 pk
        private String waterPlaceName; // 물놀이 장소 이름
        private String waterPlaceAddr; // 물놀이 장소 주소
        private Double waterPlaceRating; // 평점
        private Integer waterPlaceReviewCnt; // 리뷰수
        private String managementType; // managementType 정보 (일반지역, 중점관리 지역)
        private String waterPlaceCategory; // waterPlaceCategory 정보 (하천, 계곡)
        private String waterPlaceImageUrl;
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class WaterPlaceDetailRespDto {

        private String waterPlaceImage; // 물놀이 장소 사진
        private String waterPlaceName; // 물놀이 장소 명칭
        private String latitude; // 위도
        private String longitude; // 경도
        private String managementType; // 관리유형(일반지역, 중점관리지역)
        private String detailAddress; // 주소 + 세부지명(null)
        private String town; // 읍면(null)
        private String annualVisitors; // 연평균 총 이용객수(천명)
        private String safetyMeasures; // 안전조치 사항
        private String waterPlaceSegment; // 물놀이구간(m)
        private String dangerSegments; // 위험구역구간(null)
        private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)
        private String deepestDepth; // 수심(깊은곳)
        private String avgDepth; // 평균 수심
        private Double waterTemperature; // 계곡 수온(°C)
        private Double bod; // BOD(mg/L)
        private Double turbidity; // 탁도(NTU)
        private Map<String, Integer> waterQualityReviews;

        public static WaterPlaceDetailRespDto createWaterPlaceDetailRespDto(WaterPlace waterPlace, Coordinate coordinate, WaterPlaceDetail waterPlaceDetail, Map<String, Integer> reviewCounts) {

            ImageFile waterPlaceImage = waterPlace.getWaterPlaceImage();

            return WaterPlaceDetailRespDto.builder()
                    .waterPlaceImage((waterPlaceImage != null) ? waterPlaceImage.getStoreFileUrl() : null)
                    .waterPlaceName(waterPlace.getWaterPlaceName())
                    .latitude(coordinate.getLatitude())
                    .longitude(coordinate.getLongitude())
                    .managementType(waterPlace.getManagementType())
                    .detailAddress(waterPlace.getAddress() + " " + waterPlace.getSubLocation())
                    .town(waterPlace.getTown())
                    .annualVisitors(waterPlaceDetail.getAnnualVisitors())
                    .safetyMeasures(waterPlaceDetail.getSafetyMeasures())
                    .waterPlaceSegment(waterPlaceDetail.getWaterPlaceSegment())
                    .dangerSegments(waterPlaceDetail.getDangerSegments())
                    .dangerSignboardsNum(waterPlaceDetail.getDangerSignboardsNum())
                    .deepestDepth(waterPlaceDetail.getDeepestDepth())
                    .avgDepth(waterPlaceDetail.getAvgDepth())
                    .waterTemperature(waterPlaceDetail.getWaterTemperature())
                    .bod(waterPlaceDetail.getBod())
                    .turbidity(waterPlaceDetail.getTurbidity())
                    .waterQualityReviews(reviewCounts)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class AdminWaterPlaceDetailRespDto {

        private Long waterPlaceId; // 물놀이 장소 pk
        private String waterPlaceImage; // 물놀이 장소 사진
        private String waterPlaceName; // 물놀이 장소 명칭
        private String latitude; // 위도
        private String longitude; // 경도
        private String waterPlaceCategory; // 구분(계곡, 하천)
        private String managementType; // 관리유형(일반지역, 중점관리지역)
        private String detailAddress; // 주소 + 세부지명(null)
        private String town; // 읍면(null)
        private String annualVisitors; // 연평균 총 이용객수(천명)
        private String safetyMeasures; // 안전조치 사항
        private String waterPlaceSegment; // 물놀이구간(m)
        private String dangerSegments; // 위험구역구간(null)
        private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)
        private String deepestDepth; // 수심(깊은곳)
        private String avgDepth; // 평균 수심
        private Double waterTemperature; // 계곡 수온(°C)
        private Double bod; // BOD(mg/L)
        private Double turbidity; // 탁도(NTU)

        public static AdminWaterPlaceDetailRespDto createAdminWaterPlaceDetailRespDto(WaterPlace waterPlace, Coordinate coordinate, WaterPlaceDetail waterPlaceDetail) {

            ImageFile waterPlaceImage = waterPlace.getWaterPlaceImage();
            return AdminWaterPlaceDetailRespDto.builder()
                    .waterPlaceId(waterPlace.getId())
                    .waterPlaceImage((waterPlaceImage != null) ? waterPlaceImage.getStoreFileUrl() : null)
                    .waterPlaceName(waterPlace.getWaterPlaceName())
                    .latitude(coordinate.getLatitude())
                    .longitude(coordinate.getLongitude())
                    .waterPlaceCategory(waterPlace.getWaterPlaceCategory())
                    .managementType(waterPlace.getManagementType())
                    .detailAddress(waterPlace.getAddress() + " " + waterPlace.getSubLocation())
                    .town(waterPlace.getTown())
                    .annualVisitors(waterPlaceDetail.getAnnualVisitors())
                    .safetyMeasures(waterPlaceDetail.getSafetyMeasures())
                    .waterPlaceSegment(waterPlaceDetail.getWaterPlaceSegment())
                    .dangerSegments(waterPlaceDetail.getDangerSegments())
                    .dangerSignboardsNum(waterPlaceDetail.getDangerSignboardsNum())
                    .deepestDepth(waterPlaceDetail.getDeepestDepth())
                    .avgDepth(waterPlaceDetail.getAvgDepth())
                    .waterTemperature(waterPlaceDetail.getWaterTemperature())
                    .bod(waterPlaceDetail.getBod())
                    .turbidity(waterPlaceDetail.getTurbidity())
                    .build();
        }
    }

    @AllArgsConstructor
    @Data
    public static class NationalPopularWaterPlacesDto {

        // 계곡 이미지
        private Long waterPlaceId; // 물놀이 지역 pk
        private String waterPlaceName; // 물놀이 지역 이름
        private String location; // province + city
        private String rating; // 평점
        private Integer reviewCnt; // 리뷰 개수
        private String waterPlaceImageUrl; // 물놀이 지역 이미지
    }

    @Data
    @AllArgsConstructor
    public static class AdminRetrieveWaterPlacesDto {
        private Long waterPlaceId; // 물놀이 지역 아이디
        private String waterPlaceImageUrl; // 물놀이 지역 이미지
        private String waterPlaceName; // 물놀이 지역 이름
        private String province; // 시도
        private String city; // 시군구
        private String managementType; // 안전 관리 유형
        private Integer reviewCnt; // 리뷰 순
        private Double rating; // 평점
        private String waterPlaceCategory; // 구분
    }
}
