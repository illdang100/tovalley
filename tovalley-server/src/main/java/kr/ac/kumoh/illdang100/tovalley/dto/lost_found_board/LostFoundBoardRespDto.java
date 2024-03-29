package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class LostFoundBoardRespDto {

    @Data
    @AllArgsConstructor
    public static class LostFoundBoardListRespDto {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Long commentCnt;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime postCreateAt;
        private String postImage;
        private LostFoundEnum category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LostFoundBoardDetailRespDto {
        private String title;
        private String content;
        private String author;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime postCreateAt;
        private List<String> postImages;
        private Long commentCnt;
        private List<CommentDetailRespDto> comments;
    }

    @Data
    @AllArgsConstructor
    public static class CommentDetailRespDto {
        private String commentAuthor;
        private String commentContent;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime commentCreateAt;
        private boolean isMyComment;
    }
}
