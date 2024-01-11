package kr.ac.kumoh.illdang100.tovalley.dto.admin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

public class AdminChangeRoleReqDto {

    @AllArgsConstructor
    @Data
    public static class SearchMembersCondition {

        @NotEmpty
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{1,20}$", message = "한글/영문/숫자 1~20자 이내로 작성해주세요")
        private String nickname;
    }

    @AllArgsConstructor
    @Data
    public static class ChangeMemberRoleReqDto {

        private Long memberId;

        @NotEmpty
        @Length(max = 30)
        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;

        @NotNull
        @Pattern(regexp = "(ADMIN|CUSTOMER)$")
        private MemberEnum role;
    }
}