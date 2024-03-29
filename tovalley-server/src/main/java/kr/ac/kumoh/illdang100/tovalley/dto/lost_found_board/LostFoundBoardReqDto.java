package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

public class LostFoundBoardReqDto {

    @Data
    @AllArgsConstructor
    public static class LostFoundBoardListReqDto {
        @Pattern(regexp = "(LOST|FOUND)$")
        private String category;

        @NotEmpty
        private List<Long> valleyId;

        @Nullable
        private String searchWord;

        private boolean isResolved;
    }
}
