package kr.ac.kumoh.illdang100.tovalley.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.LostFoundBoardListReqDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.service.page.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Page", description = "페이지 API Document")
public class PageApiController {

    private final PageService pageService;
    private final JwtProcess jwtProcess;

    @GetMapping("/main-page")
    @Operation(summary = "메인 화면", description = "메인 화면을 출력합니다.")
    public ResponseEntity<?> getMainPage() {

        MainPageAllRespDto mainPageAllRespDto = pageService.getMainPageAllData();
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 조회에 성공하였습니다", mainPageAllRespDto), HttpStatus.OK);
    }

    @GetMapping("/auth/water-places/{id}")
    @Operation(summary = "물놀이 장소 상세보기 화면", description = "물놀이 장소 상세보기 페이지 정보를 반환합니다.")
    public ResponseEntity<?> getWaterPlaceDetailPage(@PathVariable("id") Long waterPlaceId,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WaterPlaceDetailPageAllRespDto result
                = pageService.getWaterPlaceDetailPageAllData(waterPlaceId, principalDetails.getMember().getId(), pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "물놀이 장소 상세보기 페이지 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/auth/my-page")
    @Operation(summary = "마이 페이지", description = "마이 페이지를 출력합니다.")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Long memberId = principalDetails.getMember().getId();

        MyPageAllRespDto result = pageService.getMyPageAllData(memberId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "마이 페이지 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @GetMapping("/lostItem")
    public ResponseEntity<?> getLostFoundBoardList(@ModelAttribute @Valid LostFoundBoardListReqDto lostFoundBoardListReqDto,
                                                   BindingResult bindingResult,
                                                   @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<LostFoundBoardListRespDto> lostFoundBoardList = pageService.getLostFoundBoardList(lostFoundBoardListReqDto, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 찾기 페이지 조회를 성공했습니다", lostFoundBoardList), HttpStatus.OK);
    }

    @GetMapping("/lostItem/{lostFoundBoardId}")
    public ResponseEntity<?> getLostFoundBoardDetail(@PathVariable Long lostFoundBoardId,
                                                     @CookieValue(value = JwtVO.ACCESS_TOKEN, required = false) String accessToken) {
        PrincipalDetails principalDetails = null;
        if (accessToken != null) {
            String token = URLDecoder.decode(accessToken, StandardCharsets.UTF_8).replace(JwtVO.TOKEN_PREFIX, "");
            principalDetails = jwtProcess.verify(token);
        }

        Optional<Member> optionalMember = Optional.ofNullable(principalDetails)
                .map(PrincipalDetails::getMember);
        LostFoundBoardDetailRespDto lostFoundBoardDetail = pageService.getLostFoundBoardDetail(lostFoundBoardId, optionalMember.orElse(null));

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 찾기 상세 페이지 조회를 성공했습니다", lostFoundBoardDetail), HttpStatus.OK);
    }
}
