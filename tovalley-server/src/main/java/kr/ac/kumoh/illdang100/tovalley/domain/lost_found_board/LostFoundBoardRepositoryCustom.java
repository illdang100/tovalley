package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.LostFoundBoardListReqDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;

public interface LostFoundBoardRepositoryCustom {
    Slice<LostFoundBoardListRespDto> getLostFoundBoardListBySlice(LostFoundBoardListReqDto lostFoundBoardListReqDto, Pageable pageable);
}
