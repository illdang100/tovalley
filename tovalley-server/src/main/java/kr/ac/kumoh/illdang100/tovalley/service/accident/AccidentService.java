package kr.ac.kumoh.illdang100.tovalley.service.accident;


import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;

public interface AccidentService {

    // 사건사고 등록
    Accident addAccident(Long waterPlaceId);

    // 사건사고 삭제
    void deleteAccident(Long accidentId);

    // 행정자치구역별 사건사고 수 조회
    AccidentCountDto getAccidentCntPerMonthByProvince(String province);

    // 물놀이 장소별 최근 5년간 사건사고 수 조회
    WaterPlaceAccidentFor5YearsDto getAccidentsFor5YearsByWaterPlace(Long waterPlaceId);

    AccidentForAdminByWaterPlace getAccidentDetailByWaterPlace(Long waterPlaceId, RetrieveAccidentCondition retrieveAccidentCondition, Pageable pageable);
}
