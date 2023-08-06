package kr.ac.kumoh.illdang100.tovalley.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

public interface WaterPlaceService {

    // 물놀이 장소 리스트 조회 페이지
    Page<RetrieveWaterPlaceDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable);
}
