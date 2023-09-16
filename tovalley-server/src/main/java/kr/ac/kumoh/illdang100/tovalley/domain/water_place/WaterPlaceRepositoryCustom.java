package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.RetrieveWaterPlacesCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

public interface WaterPlaceRepositoryCustom {
    Page<RetrieveWaterPlacesDto> findWaterPlaceList(RetrieveWaterPlacesCondition RetrieveWaterPlacesCondition, Pageable pageable);

    Page<AdminRetrieveWaterPlacesDto> getAdminWaterPlaceList(String searchWord, Pageable pageable);
}
