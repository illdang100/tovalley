package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class LostFoundBoardReqDto {

    @Data
    @AllArgsConstructor
    public static class LostFoundBoardListReqDto {
        private String category;
        private List<String> valley;
        private String searchWord;
        private boolean isResolved;
    }
}
