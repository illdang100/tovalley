package kr.ac.kumoh.illdang100.tovalley.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AdminChangeRoleReqDto {

    @AllArgsConstructor
    @Data
    public static class SearchMembersCondition {

        private String nickname;
    }
}