package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.List;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest extends DummyObject {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findSignedUpEmail_test() throws Exception {
        //given
        FindEmailReqDto findEmailReqDto = new FindEmailReqDto();
        findEmailReqDto.setEmail("test1@naver.com");

        Member member = newMockMember(1L, "test1", "nickname", MemberEnum.CUSTOMER);

        //stub
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        //when
        String findEmail = memberService.findSignedUpEmail(findEmailReqDto);

        //then
        assertThat(findEmail).isEqualTo(findEmailReqDto.getEmail());
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void resetPassword_test() throws Exception {
        //given
        ResetPasswordReqDto resetPasswordReqDto = new ResetPasswordReqDto();
        resetPasswordReqDto.setEmail("test2@naver.com");
        resetPasswordReqDto.setNewPassword("test111");

        Member member = newMockMember(2L, "test2", "nickname2", MemberEnum.CUSTOMER);

        //stub
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        String encodedPassword = "encodedNewTestPassword";
        when(passwordEncoder.encode(resetPasswordReqDto.getNewPassword())).thenReturn(encodedPassword);

        // Call the resetPassword method
        memberService.resetPassword(resetPasswordReqDto);

        //then
        assertThat(member.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logout_test() throws Exception {
        //given
        HttpServletResponse response = mock(HttpServletResponse.class);
        RefreshToken refreshToken = newRefreshToken("testRefreshToken");

        //stub
        when(refreshTokenRedisRepository.findById(any())).thenReturn(Optional.of(refreshToken));

        //when
        memberService.logout(response, "testRefreshToken");

        // Then
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response, times(3)).addCookie(cookieCaptor.capture());

        List<Cookie> capturedCookies = cookieCaptor.getAllValues();

        for (Cookie cookie : capturedCookies) {
            if (cookie.getName().equals(JwtVO.ACCESS_TOKEN)) {
                assertThat(cookie.getValue()).isEqualTo("");
            } else if (cookie.getName().equals(JwtVO.REFRESH_TOKEN)) {
                assertThat(cookie.getValue()).isEqualTo("");
            } else if (cookie.getName().equals("ISLOGIN")) {
                assertThat(cookie.getValue()).isEqualTo("false");
            }
        }
    }

}
