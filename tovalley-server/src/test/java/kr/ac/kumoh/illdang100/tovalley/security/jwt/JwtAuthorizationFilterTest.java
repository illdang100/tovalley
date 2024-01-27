package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import java.util.UUID;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.saveRefreshToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
public class JwtAuthorizationFilterTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtProcess jwtProcess;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("인가 성공 테스트")
    void successfulAuthorization_test() throws Exception {
        //given
        Member member = memberRepository.findByNickname("일당백").get();

        PrincipalDetails loginUser = new PrincipalDetails(member);
        String accessToken = jwtProcess.createAccessToken(loginUser);

        String refreshTokenId = saveRefreshToken(jwtProcess, refreshTokenRedisRepository,
                String.valueOf(member.getId()),
                member.getRole().toString(), "127.0.0.1");

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/my-page")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken),
                        new Cookie(JwtVO.REFRESH_TOKEN, refreshTokenId)));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증 실패 테스트(유효기간이 지난 토큰 가진 경우)")
    void Authorization_duration_failure_test() throws Exception {
        //given
        String refreshTokenId = UUID.randomUUID().toString();
        String accessToken = "Bearer test";

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/my-page")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken),
                        new Cookie(JwtVO.REFRESH_TOKEN, refreshTokenId)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("만료된 토큰입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("인증 실패 테스트(쿠키가 없는 경우)")
    void Authorization_no_cookie_failure_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/auth/my-page"));

        //then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("관리자 인가 테스트")
    void authorization_admin_test() throws Exception {
        //given
        Member member = memberRepository.findByNickname("일당백").get();

        PrincipalDetails loginUser = new PrincipalDetails(member);
        String accessToken = jwtProcess.createAccessToken(loginUser);

        String refreshTokenId = saveRefreshToken(jwtProcess, refreshTokenRedisRepository,
                String.valueOf(member.getId()),
                member.getRole().toString(), "127.0.0.1");

        //when
        ResultActions resultActions = mvc.perform(get("/th/admin/tovalley/test")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken),
                        new Cookie(JwtVO.REFRESH_TOKEN, refreshTokenId)));

        //then
        resultActions.andExpect(status().isForbidden());

    }

    private void dataSetting() {
        memberRepository.save(newMember("kakao123", "일당백"));
        em.clear();
    }
}
