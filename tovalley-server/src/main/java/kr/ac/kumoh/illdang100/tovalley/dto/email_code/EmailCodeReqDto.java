package kr.ac.kumoh.illdang100.tovalley.dto.email_code;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailCodeReqDto {

    @Data
    public static class SendEmailCodeReqDto {
        @NotEmpty
        @Length(max=30)
        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;
    }
}
