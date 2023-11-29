package kr.ac.kumoh.illdang100.tovalley.service.page;

import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

public interface PageService {

    MainPageAllRespDto getMainPageAllData();

    WaterPlaceDetailPageAllRespDto getWaterPlaceDetailPageAllData(Long waterPlaceId, Pageable pageable);

    MyPageAllRespDto getMyPageAllData(Long memberId, Pageable pageable);
}
