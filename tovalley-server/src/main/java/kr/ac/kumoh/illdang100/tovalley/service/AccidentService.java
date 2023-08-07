package kr.ac.kumoh.illdang100.tovalley.service;

public interface AccidentService {

    // 지역별 사건사고 조회(메인 페이지)
    AccidentByRegionRespDTO retrieveAccidentsByRegion(String province);

    // 계곡별 사건사고 조회
    AccidentByWaterPlaceRespDto retrieveAccidentsByWaterPlace(Long waterPlaceId);

    // 사건사고 등록
    AccidentDetailRespDto addAccident(Long waterPlaceId);

    // 사건사고 삭제
    void deleteAccident(Long accidentId);
}
