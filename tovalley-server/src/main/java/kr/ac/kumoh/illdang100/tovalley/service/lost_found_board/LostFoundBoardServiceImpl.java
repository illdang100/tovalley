package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService{

    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;

    /**
     * 분실물 찾기 게시글 조회
     * @param LostFoundBoardListReqDto
     * @param pageable
     * @return
     */
    @Override
    public Slice<LostFoundBoardListRespDto> getLostFoundBoardList(LostFoundBoardListReqDto LostFoundBoardListReqDto, Pageable pageable) {
        return lostFoundBoardRepository.getLostFoundBoardListBySlice(LostFoundBoardListReqDto, pageable);
    }

    /**
     * 분실물 찾기 게시글 상세 페이지
     * @param lostFoundBoardId
     * @param memberId
     * @return
     */
    @Override
    public LostFoundBoardDetailRespDto getLostFoundBoardDetail(long lostFoundBoardId, long memberId) {

        LostFoundBoard findLostFoundBoard = findLostFoundBoardByIdOrElseThrow(lostFoundBoardRepository, lostFoundBoardId);

        return LostFoundBoardDetailRespDto.builder()
                .title(findLostFoundBoard.getTitle())
                .content(findLostFoundBoard.getContent())
                .author(findLostFoundBoard.getMember().getNickname())
                .postCreateAt(findLostFoundBoard.getCreatedDate())
                .comments(findCommentDetails(lostFoundBoardId, memberId))
                .postImages(lostFoundBoardImageRepository.findImageByLostFoundBoardId(lostFoundBoardId))
                .commentCnt(commentRepository.countByLostFoundBoardId(lostFoundBoardId))
                .build();
    }

    private List<CommentDetailRespDto> findCommentDetails(long lostFoundBoardId, long memberId) {
        return commentRepository.findCommentByLostFoundBoardId(lostFoundBoardId)
                .stream()
                .map(c -> new CommentDetailRespDto(c.getAuthorEmail(), c.getContent(), c.getCreatedDate(), isMyComment(memberId, c.getAuthorEmail())))
                .collect(Collectors.toList());
    }

    private boolean isMyComment(long memberId, String authorEmail) {
        Member findMember = findMemberByEmailOrElseThrowEx(memberRepository, authorEmail);
        return (memberId == findMember.getId());
    }
}
