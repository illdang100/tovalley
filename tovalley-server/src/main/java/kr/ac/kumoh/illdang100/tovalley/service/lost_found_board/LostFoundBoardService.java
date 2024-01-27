package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;

public interface LostFoundBoardService {
    // 분실물 게시글 조회 메서드
    Slice<LostFoundBoardListRespDto> getLostFoundBoardList(LostFoundBoardListReqDto lostFoundBoardListReqDto, Pageable pageable);

    // 분실물 게시글 상세 페이지 조회 메서드
    LostFoundBoardDetailRespDto getLostFoundBoardDetail(long lostFoundBoardId, long memberId);

    // 분실물 게시글 등록

    // 분실물 게시글 수정

    // 분실물 게시글 삭제
}
