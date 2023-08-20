package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceService;
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
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "WaterPlace", description = "물놀이 장소 API Document")
public class WaterPlaceApiController {

    private final WaterPlaceService waterPlaceService;

    @GetMapping("/main-page/popular-water-places")
    @Operation(summary = "인기 물놀이 장소", description = "인기 물놀이 장소 리스트를 출력합니다.")
    public ResponseEntity<?> getPopularWaterPlaces(@ModelAttribute @Valid RetrievePopularWaterPlacesCondition popularWaterPlacesCondition,
                                                   BindingResult bindingResult) {

        List<NationalPopularWaterPlacesDto> popularWaterPlaces =
                waterPlaceService.getPopularWaterPlaces(popularWaterPlacesCondition.getCond());
        return new ResponseEntity<>(new ResponseDto<>(1, "인기 물놀이 지역 조회에 성공하였습니다", popularWaterPlaces), HttpStatus.OK);
    }
}
