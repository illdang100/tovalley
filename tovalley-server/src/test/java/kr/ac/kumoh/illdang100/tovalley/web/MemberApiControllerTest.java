package kr.ac.kumoh.illdang100.tovalley.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.ISLOGIN;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class MemberApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("자체 로그인 테스트")
    void selfLogin_test() throws Exception {
        //given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("user123@naver.com");
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
        String isLogin = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtVO.ACCESS_TOKEN)) {
                    accessToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } else if (cookie.getName().equals(JwtVO.REFRESH_TOKEN)) {
                    refreshToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } else if (cookie.getName().equals(ISLOGIN)) {
                    isLogin = cookie.getValue();
                }
            }
        }

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user123@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void updateNickname_test() throws Exception {

        // given
        Long memberId = 1L;
        ValidateNicknameRequest validateNicknameRequest = new ValidateNicknameRequest();
        validateNicknameRequest.setNickname("변경후닉네임");

        String requestBody = om.writeValueAsString(validateNicknameRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/members/set-nickname")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andReturn().getResponse().getContentAsString();

        Member findMember = memberRepository.findById(memberId).get();

        // then
        assertThat(findMember.getNickname()).isEqualTo("변경후닉네임");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUp_test() throws Exception {
        //given
        SignUpReqDto signUpReqDto = new SignUpReqDto();
        signUpReqDto.setName("홍길동");
        signUpReqDto.setEmail("hong123@kakao.com");
        signUpReqDto.setNickname("배홍홍");
        signUpReqDto.setPassword("ho!");

        String requestBody = om.writeValueAsString(signUpReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/members")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(status().isCreated());
    }

    private void dataSetting() {
        memberRepository.save(newMember("user123@naver.com", "일당백"));
        em.clear();
    }
}