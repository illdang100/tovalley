package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtProcessTest extends DummyObject {

    @Autowired
    private JwtProcess jwtProcess;

    private String createAccessToken(Long id, String username, String nickname, MemberEnum role) {
        Member member = newMockMember(id, username, nickname, role);
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        return jwtProcess.createAccessToken(principalDetails);
    }
    @Test
    @DisplayName("액세스 토큰 생성 테스트")
    void createAccessToken_test() throws Exception {
        //given

        //when
        String jwtToken1 = createAccessToken(1L, "username1", "nickname1", MemberEnum.CUSTOMER);
        String jwtToken2 = createAccessToken(2L, "username2", "nickname2", MemberEnum.CUSTOMER);

        System.out.println("jwtToken1 = " + jwtToken1);
        System.out.println("jwtToken2 = " + jwtToken2);

        //then
        assertThat(jwtToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(jwtToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void createRefreshToken_test() throws Exception {
        //given

        //when
        String refreshToken1 = jwtProcess.createRefreshToken("1", MemberEnum.CUSTOMER.toString());
        String refreshToken2 = jwtProcess.createRefreshToken("2", MemberEnum.ADMIN.toString());

        System.out.println("refreshToken1 = " + refreshToken1);
        System.out.println("refreshToken2 = " + refreshToken2);

        //then
        assertThat(refreshToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(refreshToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    void verify_test() throws Exception {
        //given
        String accessToken1 = createAccessToken(1L, "username1", "nickname1", MemberEnum.CUSTOMER);
        String accessToken2 = createAccessToken(2L, "username2", "nickname2", MemberEnum.ADMIN);

        String jwtToken1 = accessToken1.replace(JwtVO.TOKEN_PREFIX, "");
        String jwtToken2 = accessToken2.replace(JwtVO.TOKEN_PREFIX, "");

        //when
        PrincipalDetails createdPrincipalDetails1 = jwtProcess.verify(jwtToken1);
        PrincipalDetails createdPrincipalDetails2 = jwtProcess.verify(jwtToken2);
        Member createMember1 = createdPrincipalDetails1.getMember();
        Member createMember2 = createdPrincipalDetails2.getMember();

        //then
        assertThat(createMember1.getId()).isEqualTo(1L);
        assertThat(createMember2.getId()).isEqualTo(2L);

        assertThat(createMember1.getRole()).isEqualTo(MemberEnum.CUSTOMER);
        assertThat(createMember2.getRole()).isEqualTo(MemberEnum.ADMIN);
    }

}