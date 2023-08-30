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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

        String decodedJwtToken1 = URLDecoder.decode(jwtToken1, StandardCharsets.UTF_8);
        String decodedJwtToken2 = URLDecoder.decode(jwtToken2, StandardCharsets.UTF_8);

        //then
        assertThat(decodedJwtToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(decodedJwtToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void createRefreshToken_test() throws Exception {
        //given

        //when
        String refreshToken1 = jwtProcess.createRefreshToken("1", MemberEnum.CUSTOMER.toString());
        String refreshToken2 = jwtProcess.createRefreshToken("2", MemberEnum.ADMIN.toString());

        String decodedRefreshToken1 = URLDecoder.decode(refreshToken1, StandardCharsets.UTF_8);
        String decodedRefreshToken2 = URLDecoder.decode(refreshToken2, StandardCharsets.UTF_8);

        //then
        assertThat(decodedRefreshToken1.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(decodedRefreshToken2.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    void verify_test() throws Exception {
        //given
        String accessToken1 = createAccessToken(1L, "username1", "nickname1", MemberEnum.CUSTOMER);
        String accessToken2 = createAccessToken(2L, "username2", "nickname2", MemberEnum.ADMIN);

        String jwtToken1 = URLDecoder.decode(accessToken1, StandardCharsets.UTF_8).replace(JwtVO.TOKEN_PREFIX, "");
        String jwtToken2 = URLDecoder.decode(accessToken2, StandardCharsets.UTF_8).replace(JwtVO.TOKEN_PREFIX, "");

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