package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LostFoundBoardController {
    private final LostFoundBoardService lostFoundBoardService;

    @GetMapping("/lostItem")
    public ResponseEntity<?> getLostFoundBoardList(@ModelAttribute LostFoundBoardListReqDto lostFoundBoardListReqDto,
                                                   @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<LostFoundBoardListRespDto> lostFoundBoardList = lostFoundBoardService.getLostFoundBoardList(lostFoundBoardListReqDto, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 찾기 페이지 조회를 성공했습니다", lostFoundBoardList), HttpStatus.OK);
    }
}
