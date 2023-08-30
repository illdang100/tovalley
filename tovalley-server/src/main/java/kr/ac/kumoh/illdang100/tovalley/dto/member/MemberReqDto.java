package kr.ac.kumoh.illdang100.tovalley.dto.member;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class MemberReqDto {
    @Data
    public static class SignUpReqDto {

        @NotEmpty
        @Pattern(regexp = "^[가-힣a-zA-Z]{1,10}$", message = "한글/영문 1~10자 이내로 작성해주세요")
        private String name;

        @NotEmpty
        @Length(max=30)
        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;

        @NotEmpty
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{1,20}$", message = "한글/영문/숫자 1~20자 이내로 작성해주세요")
        private String nickname;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?]{1,20}$", message = "영문/숫자/특수 기호 1~20자 이내로 작성해주세요") // 영문, 숫자, 특수 기호
        private String password;
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

