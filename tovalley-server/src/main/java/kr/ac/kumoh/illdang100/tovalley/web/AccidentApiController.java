package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Accident", description = "사건사고 API Document")
public class AccidentApiController {

    private final AccidentService accidentService;

    @GetMapping("/main-page/accidents")
    @Operation(summary = "올해 물놀이 사건 사고", description = "올해 사고 발생 수를 출력합니다.")
    public ResponseEntity<?> getAccidentsByProvince(@ModelAttribute @Valid RetrieveYearlyAccidentCondition yearlyAccidentCondition,
                                                    BindingResult bindingResult) {

        AccidentCountDto totalAccidents =
                accidentService.getAccidentCntPerMonthByProvince(yearlyAccidentCondition.getProvince().getValue());
        return new ResponseEntity<>(new ResponseDto<>(1, "올해 사고 발생수 조회에 성공하였습니다", totalAccidents), HttpStatus.OK);
    }
}
