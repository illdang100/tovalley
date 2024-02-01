package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.*;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.util.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService{

    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;
    private final WaterPlaceRepository waterPlaceRepository;

    @Override
    @Transactional
    public Long saveLostFoundBoard(LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto, Member member) {
        WaterPlace findWaterPlace = EntityFinder.findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, lostFoundBoardSaveReqDto.getValleyId());

        LostFoundBoard lostFoundBoard = lostFoundBoardRepository.save(LostFoundBoard.builder()
                .member(member)
                .lostFoundEnum(LostFoundEnum.valueOf(lostFoundBoardSaveReqDto.getCategory()))
                .title(lostFoundBoardSaveReqDto.getTitle())
                .content(lostFoundBoardSaveReqDto.getContent())
                .waterPlace(findWaterPlace)
                .build());

        return lostFoundBoard.getId();
    }
}
