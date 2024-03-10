package kr.ac.kumoh.illdang100.tovalley.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentRespDto {
    @Data
    @Builder
    @AllArgsConstructor
    public static class CommentSaveRespDto {
        private Long commentId;
        private String commentAuthor;
        private String commentContent;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime commentCreateAt;
        private String commentAuthorProfile;

        public static CommentSaveRespDto createCommentSaveRespDto(Comment comment, Member member) {
            return CommentSaveRespDto.builder()
                    .commentId(comment.getId())
                    .commentAuthor(member.getNickname())
                    .commentContent(comment.getContent())
                    .commentCreateAt(comment.getCreatedDate())
                    .commentAuthorProfile(member.getImageFile().getStoreFileUrl())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    public static class CommentDetailRespDto {
        private Long commentId;
        private String commentAuthor;
        private String commentContent;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime commentCreateAt;
        private boolean commentByUser;
        private String commentAuthorProfile;
    }
}
