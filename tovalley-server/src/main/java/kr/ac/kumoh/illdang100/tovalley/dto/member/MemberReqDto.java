package kr.ac.kumoh.illdang100.tovalley.dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberReqDto {

    @Data
    public static class SignUpReqDto {

    }

    @Data
    public static class LoginReqDto {
        private String username;
        private String password;
    }

    @Data
    public static class ValidateNicknameRequest {

        @NotBlank
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{1,20}$", message = "한글/영문/숫자(0~9) 1~20자 이내로 작성해주세요")
        private String nickname;
    }
}
