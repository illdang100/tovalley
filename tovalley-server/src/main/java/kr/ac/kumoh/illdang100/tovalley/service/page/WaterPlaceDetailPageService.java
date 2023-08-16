package kr.ac.kumoh.illdang100.tovalley.service.page;

import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.WaterPlaceDetailPageRespDto.*;

public interface WaterPlaceDetailPageService {

    WaterPlaceDetailAllRespDto getWaterPlaceDetailPageAllData(Long waterPlaceId, Pageable pageable);
}
