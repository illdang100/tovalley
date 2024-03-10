package kr.ac.kumoh.illdang100.tovalley.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentReqDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentSaveReqDto {
        @NotBlank
        @Size(max = 256, message = "내용은 256자 이하로 작성해주세요")
        private String commentContent;
    }
}
