package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.PageRespDto.*;

public interface PageService {

    MainPageAllRespDto getMainPageAllData();

    WaterPlaceDetailPageAllRespDto getWaterPlaceDetailPageAllData(Long waterPlaceId, Long memberId, Pageable pageable);

    MyPageAllRespDto getMyPageAllData(Long memberId, Pageable pageable);

    Slice<LostFoundBoardListRespDto> getLostFoundBoardList(LostFoundBoardListReqDto lostFoundBoardListReqDto, Pageable pageable);

    LostFoundBoardDetailRespDto getLostFoundBoardDetail(long lostFoundBoardId, Member member);
}
