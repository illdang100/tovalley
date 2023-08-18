package kr.ac.kumoh.illdang100.tovalley.web;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto;
import kr.ac.kumoh.illdang100.tovalley.service.domain.water_place.WaterPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class WaterPlaceApiController {

    private final WaterPlaceService waterPlaceService;

    @PostMapping("/auth/valleys/list")
    public ResponseEntity<?> getWaterPlaces(@ModelAttribute @Valid RetrieveWaterPlacesCondition retrieveWaterPlacesCondition,
                                                    BindingResult bindingResult,
                                                    @PageableDefault(size = 5, sort = "rating", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WaterPlaceRespDto.RetrieveWaterPlacesDto> result = waterPlaceService.getWaterPlaces(retrieveWaterPlacesCondition, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소 리스트 조회를 성공하였습니다.", result), HttpStatus.OK);
    }
}
