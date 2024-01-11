package kr.ac.kumoh.illdang100.tovalley.dto.admin;

import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminChangeRoleRespDto {

    @Data
    public static class SearchMembersRespDto {

        private Long id;
        private String email;
        private String memberName;
        private String nickname;
        private String role; // ADMIN, CUSTOMER

        public SearchMembersRespDto(Long id, String email, String memberName, String nickname, MemberEnum role) {
            this.id = id;
            this.email = email;
            this.memberName = memberName;
            this.nickname = nickname;
            this.role = role.toString();
        }
    }
}
