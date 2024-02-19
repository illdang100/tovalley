package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.*;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService {

    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;
    private final WaterPlaceRepository waterPlaceRepository;

    @Override
    @Transactional
    public Long saveLostFoundBoard(LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto, Long memberId) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);

        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, lostFoundBoardSaveReqDto.getValleyId());

        LostFoundBoard lostFoundBoard = lostFoundBoardRepository.save(LostFoundBoard.builder()
                .member(findMember)
                .lostFoundEnum(LostFoundEnum.valueOf(lostFoundBoardSaveReqDto.getCategory()))
                .title(lostFoundBoardSaveReqDto.getTitle())
                .content(lostFoundBoardSaveReqDto.getContent())
                .waterPlace(findWaterPlace)
                .build());

        return lostFoundBoard.getId();
    }

    @Override
    @Transactional
    public LostFoundBoard updateLostFoundBoard(LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto, Long memberId) {
        LostFoundBoard findLostFoundBoard = findLostFoundBoardByIdWithMemberOrElseThrowEx(lostFoundBoardRepository, lostFoundBoardUpdateReqDto.getLostFoundBoardId());
        if(!isAuthorizedToAccessBoard(findLostFoundBoard, memberId)) {
            throw new CustomApiException("게시글 작성자에게만 수정 권한이 있습니다");
        }

        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, lostFoundBoardUpdateReqDto.getValleyId());
        findLostFoundBoard.updateLostFoundBoard(findWaterPlace, lostFoundBoardUpdateReqDto.getTitle(), lostFoundBoardUpdateReqDto.getContent(), LostFoundEnum.valueOf(lostFoundBoardUpdateReqDto.getCategory()));

        return findLostFoundBoard;
    }

    @Override
    public Boolean isAuthorizedToAccessBoard(LostFoundBoard lostFoundBoard, Long memberId) {
        boolean result = false;
        Member member = lostFoundBoard.getMember();
        if (member != null) {
            result = lostFoundBoard.getMember().getId().equals(memberId);
        }
        return result;
    }

    @Override
    @Transactional
    public void updateResolvedStatus(Long lostFoundBoardId, Boolean isResolved) {
        LostFoundBoard findLostFoundBoard = findLostFoundBoardOrElseThrowEx(lostFoundBoardRepository, lostFoundBoardId);
        findLostFoundBoard.updateResolvedStatus(isResolved);
    }
}
