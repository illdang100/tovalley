package kr.ac.kumoh.illdang100.tovalley.service.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

public interface WaterPlaceService {

    // 물놀이 장소 리스트 조회 페이지
    Page<RetrieveWaterPlaceDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable);

    // 평점순 || 리뷰순 인기 물놀이 지역 리스트(4개) 조회
    List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond);
}
