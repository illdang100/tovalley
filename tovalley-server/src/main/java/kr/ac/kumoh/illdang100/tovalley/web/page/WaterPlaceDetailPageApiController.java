package kr.ac.kumoh.illdang100.tovalley.web.page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.page.WaterPlaceDetailPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.WaterPlaceDetailPageRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "WaterPlaceDetailPage", description = "계곡 상세보기 페이지 API Document")
public class WaterPlaceDetailPageApiController {

    private final WaterPlaceDetailPageService waterPlaceDetailPageService;

    @GetMapping("/auth/water-places/{id}")
    @Operation(summary = "물놀이 장소 상세보기 화면", description = "물놀이 장소 상세보기 페이지 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceDetailPage(@PathVariable("id") Long waterPlaceId,
                                                     @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WaterPlaceDetailAllRespDto result = waterPlaceDetailPageService.getWaterPlaceDetailPageAllData(waterPlaceId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소 상세보기 페이지 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
