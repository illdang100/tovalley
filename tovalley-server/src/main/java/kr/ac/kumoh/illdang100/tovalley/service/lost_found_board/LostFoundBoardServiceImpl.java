package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService{

    private final LostFoundBoardRepository lostFoundBoardRepository;


    @Override
    public Slice<LostFoundBoardListRespDto> getLostFoundBoardList(LostFoundBoardListReqDto LostFoundBoardListReqDto, Pageable pageable) {
        return lostFoundBoardRepository.getLostFoundBoardListBySlice(LostFoundBoardListReqDto, pageable);
    }
}
