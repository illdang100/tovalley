package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
public class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() throws Exception {
        memberRepository.save(newMember("username@naver.com", "nickname"));
    }

    @Test
    @DisplayName("인증 필터 성공 테스트")
    void successfulAuthentication_test() throws Exception {
        //given
        MemberReqDto.LoginReqDto loginReqDto = new MemberReqDto.LoginReqDto();
        loginReqDto.setUsername("username@naver.com");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String responseBody = response.getContentAsString();
        Cookie[] cookies = response.getCookies();

        String accessToken = null;
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieValue = cookie.getValue();
                if (cookie.getName().equals("accessToken")) {
                    accessToken = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
                } else if (cookie.getName().equals("refreshToken")) {
                    refreshToken = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
                }
            }
        }
        //then
        resultActions.andExpect(status().isOk());
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
        assertThat(refreshToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    @DisplayName("인증 필터 실패 테스트")
    void unSuccessfulAuthentication_test() throws Exception {
        //given
        MemberReqDto.LoginReqDto loginReqDto = new MemberReqDto.LoginReqDto();
        loginReqDto.setUsername("test@naver.com");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        Cookie[] cookies = response.getCookies();

        String accessToken = null;
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtVO.ACCESS_TOKEN)) {
                    accessToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } else if (cookie.getName().equals(JwtVO.REFRESH_TOKEN)) {
                    refreshToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }

        //then
        resultActions.andExpect(status().isBadRequest());
        assertThat(accessToken).isNull();
        assertThat(refreshToken).isNull();
    }
}
