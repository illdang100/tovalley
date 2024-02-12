package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;

public interface LostFoundBoardService {
    // 분실물 게시글 등록
    Long saveLostFoundBoard(LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto, long memberId);

    // 분실물 게시글 수정
    void updateLostFoundBoard(LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto, long memberId);

    // 분실물 게시글 삭제
}
