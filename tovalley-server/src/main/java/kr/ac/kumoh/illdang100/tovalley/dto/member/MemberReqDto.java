package kr.ac.kumoh.illdang100.tovalley.dto.member;

import lombok.Data;

public class MemberReqDto {

    @Data
    public static class SignUpReqDto {

    }

    @Data
    public static class LoginReqDto {
        private String username;
        private String password;
    }
}
