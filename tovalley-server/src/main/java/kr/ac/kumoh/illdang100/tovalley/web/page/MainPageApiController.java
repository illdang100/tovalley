package kr.ac.kumoh.illdang100.tovalley.web.page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.domain.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.domain.water_place.WaterPlaceService;
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
@Tag(name = "MainPage", description = "메인페이지 API Document")
public class MainPageApiController {

    private final MainPageService mainPageService;
    private final AccidentService accidentService;
    private final WaterPlaceService waterPlaceService;

    @GetMapping
    @Operation(summary = "메인 화면", description = "메인 화면을 출력합니다.")
    public ResponseEntity<?> getMainPage() {

        MainPageAllRespDto mainPageAllRespDto = mainPageService.getMainPageAllData();
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 조회에 성공하였습니다", mainPageAllRespDto), HttpStatus.OK);
    }

    @GetMapping("/accidents")
    @Operation(summary = "올해 물놀이 사건 사고", description = "올해 사고 발생 수를 출력합니다.")
    public ResponseEntity<?> getAccidentsByProvince(@ModelAttribute @Valid RetrieveYearlyAccidentCondition yearlyAccidentCondition,
                                                   BindingResult bindingResult) {

        AccidentCountDto totalAccidents =
                accidentService.getAccidentCntPerMonthByProvince(yearlyAccidentCondition.getProvince().getValue());
        return new ResponseEntity<>(new ResponseDto<>(1, "올해 사고 발생수 조회에 성공하였습니다", totalAccidents), HttpStatus.OK);
    }

    @GetMapping("/popular-water-places")
    @Operation(summary = "인기 물놀이 장소", description = "인기 물놀이 장소 리스트를 출력합니다.")
    public ResponseEntity<?> getPopularWaterPlaces(@ModelAttribute @Valid RetrievePopularWaterPlacesCondition popularWaterPlacesCondition,
                                                   BindingResult bindingResult) {

        List<NationalPopularWaterPlacesDto> popularWaterPlaces =
                waterPlaceService.getPopularWaterPlaces(popularWaterPlacesCondition.getCond());
        return new ResponseEntity<>(new ResponseDto<>(1, "인기 물놀이 지역 조회에 성공하였습니다", popularWaterPlaces), HttpStatus.OK);
    }
}
