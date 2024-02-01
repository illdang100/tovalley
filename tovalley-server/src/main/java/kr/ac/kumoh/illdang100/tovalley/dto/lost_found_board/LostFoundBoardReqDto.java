package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LostFoundBoardSaveReqDto {
        @Pattern(regexp = "(LOST|FOUND)$")
        private String category;

        @NotNull
        private Long valleyId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<MultipartFile> postImage;
    }
}
