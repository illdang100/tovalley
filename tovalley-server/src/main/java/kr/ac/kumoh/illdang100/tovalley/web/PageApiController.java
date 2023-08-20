package kr.ac.kumoh.illdang100.tovalley.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.page.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Page", description = "페이지 API Document")
public class PageApiController {

    private final PageService pageService;

    @GetMapping("/main-page")
    @Operation(summary = "메인 화면", description = "메인 화면을 출력합니다.")
    public ResponseEntity<?> getMainPage() {

        MainPageAllRespDto mainPageAllRespDto = pageService.getMainPageAllData();
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 조회에 성공하였습니다", mainPageAllRespDto), HttpStatus.OK);
    }

    @GetMapping("/auth/water-places/{id}")
    @Operation(summary = "물놀이 장소 상세보기 화면", description = "물놀이 장소 상세보기 페이지 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceDetailPage(@PathVariable("id") Long waterPlaceId,
                                                     @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WaterPlaceDetailPageAllRespDto result = pageService.getWaterPlaceDetailPageAllData(waterPlaceId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소 상세보기 페이지 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "마이 페이지", description = "마이 페이지를 출력합니다.")
    public ResponseEntity<?> getMyPage(Model model) {


        return new ResponseEntity<>(new ResponseDto<>(1, "마이 페이지 조회에 성공하였습니다", null), HttpStatus.OK);
    }
}
