package kr.ac.kumoh.illdang100.tovalley.service.comment;

import kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentReqDto.CommentSaveReqDto;

import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.*;

public interface CommentService {
    void deleteCommentByLostFoundBoardIdInBatch(Long lostFoundBoardId);

    CommentSaveRespDto saveComment(Long lostFoundBoardId, CommentSaveReqDto commentSaveReqDto, Long memberId);

    void deleteComment(Long lostFoundBoardId, Long commentId, Long memberId);
}
