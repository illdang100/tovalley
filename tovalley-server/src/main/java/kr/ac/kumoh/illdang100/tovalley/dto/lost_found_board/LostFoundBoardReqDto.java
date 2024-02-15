package kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.*;
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
        @Size(max = 20, message = "제목은 20자 이하로 작성해주세요")
        private String title;

        @NotBlank
        @Size(max = 256, message = "내용은 256자 이하로 작성해주세요")
        private String content;

        @Size(max = 5, message = "사진은 최대 5개까지 추가할 수 있습니다")
        private List<MultipartFile> postImage;
    }

    @Data
    @AllArgsConstructor
    public static class LostFoundBoardUpdateReqDto {
        @NotNull
        private Long lostFoundBoardId;

        @Pattern(regexp = "(LOST|FOUND)$")
        private String category;

        @NotNull
        private Long valleyId;

        @NotBlank
        @Size(max = 20, message = "제목은 20자 이하로 작성해주세요")
        private String title;

        @NotBlank
        @Size(max = 256, message = "내용은 256자 이하로 작성해주세요")
        private String content;

        private List<MultipartFile> postImage;

        private List<String> deleteImage;
    }
}
