package kr.ac.kumoh.illdang100.tovalley.service.water_place;

import kr.ac.kumoh.illdang100.tovalley.form.water_place.CreateWaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.WaterPlaceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

public interface WaterPlaceService {

    // 물놀이 장소 리스트 조회 페이지
    Page<RetrieveWaterPlacesDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable);

    // 평점순 || 리뷰순 인기 물놀이 지역 리스트(4개) 조회
    List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond);

    WaterPlaceDetailRespDto getWaterPlaceDetailByWaterPlace(Long waterPlaceId);

    RescueSupplyByWaterPlaceRespDto getRescueSuppliesByWaterPlace(Long waterPlaceId);

    AdminWaterPlaceDetailRespDto getAdminWaterPlaceDetailByWaterPlace(Long waterPlaceId);

    void updateWaterPlace(Long waterPlaceId, WaterPlaceForm form);

    void updateWaterPlaceImage(Long waterPlaceId, MultipartFile waterPlaceImage);

    void saveNewWaterPlace(CreateWaterPlaceForm form);
}
