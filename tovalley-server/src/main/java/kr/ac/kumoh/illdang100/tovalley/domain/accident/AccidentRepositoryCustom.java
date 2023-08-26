package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;

public interface AccidentRepositoryCustom {

    Page<AccidentDetailRespDto> findAccidentDetailRespDtoByWaterPlaceId(Long waterPlaceId, RetrieveAccidentCondition retrieveAccidentCondition, Pageable pageable);
}
