package kr.ac.kumoh.illdang100.tovalley.service.domain.accident;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;

public interface AccidentService {

    // 지역별 사건사고 조회(메인 페이지)
//    AccidentByRegionRespDTO retrieveAccidentsByRegion(String province);

    // 계곡별 사건사고 조회
//    AccidentByWaterPlaceRespDto retrieveAccidentsByWaterPlace(Long waterPlaceId);

    // 사건사고 등록
//    AccidentDetailRespDto addAccident(Long waterPlaceId);

    // 사건사고 삭제
    void deleteAccident(Long accidentId);

    // 행정자치구역별 사건사고 수 조회
    AccidentCountDto getAccidentCntPerMonthByProvince(String province);
}
