package kr.ac.kumoh.illdang100.tovalley.service.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentReqDto.CommentSaveReqDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.CommentSaveRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

import static kr.ac.kumoh.illdang100.tovalley.util.ListUtil.isEmptyList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LostFoundBoardRepository lostFoundBoardRepository;

    @Override
    public void deleteCommentByLostFoundBoardIdInBatch(Long lostFoundBoardId) {
        List<Comment> findCommentList = commentRepository.findCommentByLostFoundBoardId(lostFoundBoardId);

        if (!isEmptyList(findCommentList)) {
            commentRepository.deleteAllByIdInBatch(findCommentList.stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional
    public CommentSaveRespDto saveComment(Long lostFoundBoardId, CommentSaveReqDto commentSaveReqDto, Long memberId) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        LostFoundBoard findLostFoundBoard = findLostFoundBoardOrElseThrowEx(lostFoundBoardRepository, lostFoundBoardId);

        Comment savedComment = commentRepository.save(Comment.builder()
                .content(commentSaveReqDto.getCommentContent())
                .member(findMember)
                .lostFoundBoardId(findLostFoundBoard.getId())
                .build());

        return createCommentSaveRespDto(savedComment, findMember);
    }

    @Override
    @Transactional
    public void deleteComment(Long lostFoundBoardId, Long commentId, Long memberId) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        Comment findComment = findCommentByIdWithMemberOrElseThrowEx(commentRepository, commentId);

        if (!isMyComment(findComment, findMember.getId())) {
            throw new CustomApiException("댓글 작성자에게만 권한이 있습니다");
        }
        commentRepository.delete(findComment);
    }

    private Boolean isMyComment(Comment comment, Long memberId) {
        return comment.getMember().getId().equals(memberId);
    }
}
