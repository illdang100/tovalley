package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JwtAuthorizationFilterTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtProcess jwtProcess;

    @Test
    @DisplayName("인가 성공 테스트")
    void successfulAuthorization_test() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("kakao_123456789")
                .nickname("일당백")
                .role(MemberEnum.CUSTOMER)
                .build();

        PrincipalDetails loginUser = new PrincipalDetails(member);
        String accessToken = jwtProcess.createAccessToken(loginUser);
        String refreshToken = jwtProcess.createRefreshToken(member.getId().toString(), member.getRole().toString(), "127.0.0.1");

        String decodedAccessToken = URLDecoder.decode(accessToken, StandardCharsets.UTF_8);
        String decodedRefreshToken = URLDecoder.decode(refreshToken, StandardCharsets.UTF_8);

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/tovalley/test")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, decodedAccessToken),
                        new Cookie(JwtVO.REFRESH_TOKEN, decodedRefreshToken)));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("인가 실패 테스트")
    void unSuccessfulAuthorization_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/tovalley/test"));

        //then
        resultActions.andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("관리자 인가 테스트")
    void authorization_admin_test() throws Exception {
        //given
        Member member = Member.builder()
                .id(1L)
                .username("kakao_1234567678")
                .nickname("인당천")
                .role(MemberEnum.CUSTOMER)
                .build();

        PrincipalDetails loginUser = new PrincipalDetails(member);
        String accessToken = jwtProcess.createAccessToken(loginUser);
        String refreshToken = jwtProcess.createRefreshToken(member.getId().toString(), member.getRole().toString(), "127.0.0.1");

        String decodedAccessToken = URLDecoder.decode(accessToken, StandardCharsets.UTF_8);
        String decodedRefreshToken = URLDecoder.decode(refreshToken, StandardCharsets.UTF_8);

        //when
        ResultActions resultActions = mvc.perform(get("/th/admin/tovalley/test")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, decodedAccessToken),
                        new Cookie(JwtVO.REFRESH_TOKEN, decodedRefreshToken)));

        //then
        resultActions.andExpect(status().isForbidden());

    }
}
