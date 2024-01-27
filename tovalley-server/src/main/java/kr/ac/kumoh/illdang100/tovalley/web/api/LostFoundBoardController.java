package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
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

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LostFoundBoardController {
    private final LostFoundBoardService lostFoundBoardService;

    @GetMapping("/lostItem")
    public ResponseEntity<?> getLostFoundBoardList(@ModelAttribute @Valid LostFoundBoardListReqDto lostFoundBoardListReqDto,
                                                   BindingResult bindingResult,
                                                   @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<LostFoundBoardListRespDto> lostFoundBoardList = lostFoundBoardService.getLostFoundBoardList(lostFoundBoardListReqDto, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 찾기 페이지 조회를 성공했습니다", lostFoundBoardList), HttpStatus.OK);
    }

    @GetMapping("/lostItem/{lostFoundBoardId}")
    public ResponseEntity<?> getLostFoundBoardDetail(@PathVariable long lostFoundBoardId,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {

        LostFoundBoardDetailRespDto lostFoundBoardDetail = lostFoundBoardService.getLostFoundBoardDetail(lostFoundBoardId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 찾기 상세 페이지 조회를 성공했습니다", lostFoundBoardDetail), HttpStatus.OK);
    }
}
