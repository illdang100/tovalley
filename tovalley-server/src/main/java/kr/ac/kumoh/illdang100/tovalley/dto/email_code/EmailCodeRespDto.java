package kr.ac.kumoh.illdang100.tovalley.dto.email_code;

import lombok.AllArgsConstructor;
import lombok.Data;

public class EmailCodeRespDto {

    @Data
    @AllArgsConstructor
    public static class SendEmailCodeRespDto {
        private String verifyCode;
    }
}
