package kr.ac.kumoh.illdang100.tovalley.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MemberRespDto {

    @AllArgsConstructor
    @Data
    public static class MemberProfileRespDto {

        private String memberProfileImg;
        private String memberName;
        private String memberNick;
    }
}
