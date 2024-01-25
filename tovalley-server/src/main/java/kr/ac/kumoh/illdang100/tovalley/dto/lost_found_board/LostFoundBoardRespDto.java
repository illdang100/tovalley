package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LostFoundBoardRespDto {

    @Data
    @AllArgsConstructor
    public static class LostFoundBoardListRespDto {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Long commentCnt;
        private LocalDateTime postCreateAt;
        private String postImage;
        private LostFoundEnum category;
    }
}
