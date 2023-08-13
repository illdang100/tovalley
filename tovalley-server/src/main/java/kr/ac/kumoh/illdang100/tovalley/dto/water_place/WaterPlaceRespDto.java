package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import lombok.AllArgsConstructor;
import lombok.Data;

public class WaterPlaceRespDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesDto {

        // 물놀이 장소 pk
        // 물놀이 장소 이름
        // town 정보(null일 수 있음)
        // 평점
        // 리뷰수
        // managementType 정보 (일반지역, 중점관리 지역)
        // waterPlaceCategory 정보 (일반지역, 중점관리지역)
    }
}
