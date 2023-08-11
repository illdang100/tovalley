package kr.ac.kumoh.illdang100.tovalley.web.page;

import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.page.MainPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main-page")
@Slf4j
public class MainPageApiController {

    private final MainPageService mainPageService;

    @GetMapping
    public ResponseEntity<?> getMainPage() {

        MainPageAllRespDto mainPageAllRespDto = mainPageService.getMainPageAllData();
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 조회에 성공하였습니다", mainPageAllRespDto), HttpStatus.OK);
    }

    @GetMapping("/accidents")
    public ResponseEntity<?> getAccidentsByProvince(@ModelAttribute @Valid RetrieveYearlyAccidentCondition yearlyAccidentCondition,
                                                   BindingResult bindingResult) {

        AccidentCountDto totalAccidents =
                mainPageService.getTotalAccidents(yearlyAccidentCondition.getProvince().getValue());
        return new ResponseEntity<>(new ResponseDto<>(1, "올해 사고 발생수 조회에 성공하였습니다", totalAccidents), HttpStatus.OK);
    }

    @GetMapping("/popular-water-places")
    public ResponseEntity<?> getPopularWaterPlaces(@ModelAttribute @Valid RetrievePopularWaterPlacesCondition popularWaterPlacesCondition,
                                                   BindingResult bindingResult) {

        List<NationalPopularWaterPlacesDto> popularWaterPlaces = mainPageService.getPopularWaterPlaces(popularWaterPlacesCondition.getCond());
        return new ResponseEntity<>(new ResponseDto<>(1, "인기 물놀이 지역 조회에 성공하였습니다", popularWaterPlaces), HttpStatus.OK);
    }
}
