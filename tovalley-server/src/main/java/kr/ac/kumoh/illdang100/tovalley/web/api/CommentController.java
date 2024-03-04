package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/auth/lostItem/{lostFoundBoardId}/comment")
    public ResponseEntity<?> saveComment(@PathVariable("lostFoundBoardId")Long lostFoundBoardId,
                                         @RequestBody @Valid CommentSaveReqDto commentSaveReqDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CommentSaveRespDto commentSaveRespDto = commentService.saveComment(lostFoundBoardId, commentSaveReqDto, principalDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "댓글이 정상적으로 등록되었습니다", commentSaveRespDto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/auth/lostItem/{lostFoundBoardId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("lostFoundBoardId")Long lostFoundBoardId,
                                           @PathVariable("commentId")Long commentId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        commentService.deleteComment(lostFoundBoardId, commentId, principalDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "댓글이 정상적으로 삭제되었습니다", null), HttpStatus.OK);
    }
}
