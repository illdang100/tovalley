package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;

public interface LostFoundBoardService {
    // 분실물 게시글 등록
    Long saveLostFoundBoard(LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto, Long memberId);

    // 분실물 게시글 수정
    LostFoundBoard updateLostFoundBoard(LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto, Long memberId);

    // 분실물 게시글 삭제
    void deleteLostFoundBoard(Long lostFoundBoardId, Long memberId);

    Boolean isAuthorizedToAccessBoard(LostFoundBoard lostFoundBoard, Long memberId);

    void updateResolvedStatus(Long lostFoundBoardId, Boolean isResolved, Long memberId);
}
