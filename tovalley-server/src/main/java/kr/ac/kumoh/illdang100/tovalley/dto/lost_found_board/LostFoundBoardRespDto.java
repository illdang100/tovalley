package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.*;

public class LostFoundBoardRespDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class LostFoundBoardListRespDto {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Long commentCnt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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
        private String waterPlaceName;
        private String waterPlaceAddress;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postCreateAt;
        private List<String> postImages;
        private Boolean isResolved;
        private Boolean isMyBoard;
        private String boardAuthorProfile;
        private Long commentCnt;
        private List<CommentDetailRespDto> comments;

        public static LostFoundBoardDetailRespDto createLostFoundBoardDetailRespDto(LostFoundBoard lostFoundBoard, Boolean isMyBoard, List<CommentDetailRespDto> commentDetailRespDtoList, List<String> postImages, Long commentCnt) {
            return LostFoundBoardDetailRespDto.builder()
                    .title(lostFoundBoard.getTitle())
                    .content(lostFoundBoard.getContent())
                    .author(lostFoundBoard.getMember().getNickname())
                    .waterPlaceName(lostFoundBoard.getWaterPlace().getWaterPlaceName())
                    .waterPlaceAddress(lostFoundBoard.getWaterPlace().getAddress())
                    .postCreateAt(lostFoundBoard.getCreatedDate())
                    .isResolved(lostFoundBoard.getIsResolved())
                    .isMyBoard(isMyBoard)
                    .boardAuthorProfile(lostFoundBoard.getMember().getImageFile() != null ? lostFoundBoard.getMember().getImageFile().getStoreFileUrl(): null)
                    .comments(commentDetailRespDtoList)
                    .postImages(postImages)
                    .commentCnt(commentCnt)
                    .build();
        }
    }

    @AllArgsConstructor
    @Data
    public static class MyLostFoundBoardRespDto {
        private Long lostFoundBoardId;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime postCreateAt;
    }

    @Data
    @AllArgsConstructor
    public static class RecentLostFoundBoardRespDto {
        private Long lostFoundBoardId;
        private String lostFoundBoardCategory;
        private String lostFoundBoardTitle;
        private String lostFoundBoardContent;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime lostFoundBoardCreatedAt;
    }
}
