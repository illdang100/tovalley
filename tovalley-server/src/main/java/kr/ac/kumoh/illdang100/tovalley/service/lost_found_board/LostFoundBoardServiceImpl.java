package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
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

import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.AuthorizationUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService {

    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final WaterPlaceRepository waterPlaceRepository;
    private final LostFoundBoardImageService lostFoundBoardImageService;

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
        LostFoundBoard findLostFoundBoard = findLostFoundBoardByIdWithMemberOrElseThrow(lostFoundBoardRepository, lostFoundBoardUpdateReqDto.getLostFoundBoardId());
        if (!isAuthorizedToAccessBoard(findLostFoundBoard, memberId)) {
            throw new CustomApiException("게시글 작성자에게만 수정 권한이 있습니다");
        }

        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, lostFoundBoardUpdateReqDto.getValleyId());
        findLostFoundBoard.updateLostFoundBoard(findWaterPlace, lostFoundBoardUpdateReqDto.getTitle(), lostFoundBoardUpdateReqDto.getContent(), LostFoundEnum.valueOf(lostFoundBoardUpdateReqDto.getCategory()));

        return findLostFoundBoard;
    }

    @Override
    @Transactional
    public void deleteLostFoundBoard(Long lostFoundBoardId, Long memberId) {

        LostFoundBoard findLostFoundBoard = findLostFoundBoardByIdWithMemberOrElseThrow(lostFoundBoardRepository, lostFoundBoardId);

        isAuthorizedToAccessBoard(findLostFoundBoard, memberId);

        deleteCommentByLostFoundBoardIdInBatch(lostFoundBoardId);

        lostFoundBoardRepository.delete(findLostFoundBoard);
    }

    public void deleteCommentByLostFoundBoardIdInBatch(Long lostFoundBoardId) {
        List<Comment> findCommentList = commentRepository.findCommentByLostFoundBoardId(lostFoundBoardId);

        if (!findCommentList.isEmpty()) {
            commentRepository.deleteAllByIdInBatch(findCommentList.stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList()));
        }
    }
}
