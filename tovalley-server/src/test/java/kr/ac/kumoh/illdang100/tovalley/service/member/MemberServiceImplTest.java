package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
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


import java.time.LocalDate;
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

    @Mock private WaterPlaceRepository waterPlaceRepository;
    @Mock private TripScheduleRepository tripScheduleRepository;
    @Mock private ReviewRepository reviewRepository;

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

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMember_test() throws Exception {
        // given
        Member member = newMockMember(3L, "test3", "nickname3", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(1L, "물놀이 장소1", "경상북도", 3.0, 1);
        TripSchedule tripSchedule = newMockTripSchedule(1L, member, waterPlace, LocalDate.now().plusDays(1), 3);
        Review review = newReview(1L, waterPlace, tripSchedule, "물이 깨끗해요", 3, WaterQualityReviewEnum.CLEAN);

        // stub
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(tripScheduleRepository.findTripSchedulesByMemberId(any())).thenReturn(List.of(tripSchedule));

        // when
        memberService.deleteMember(member.getId(), "refreshToken");

        // then
        verify(reviewRepository, times(1)).deleteAllByTripScheduleIds(eq(List.of(review.getId())));
        verify(reviewRepository, times(1)).deleteAllByTripScheduleIds(eq(List.of(tripSchedule.getId())));
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    @DisplayName("사용자 닉네임 필수 값 - null")
    void isEmptyMemberNicknameNullTest() {
        // given
        Member member = newMockMember(1L, "test3", null, MemberEnum.CUSTOMER);

        // when
        Boolean hasNickname = memberService.isEmptyMemberNickname(member);

        // then
        assertThat(hasNickname).isTrue();
    }

    @Test
    @DisplayName("사용자 닉네임 필수 값 - 빈 문자열")
    void isEmptyMemberNicknameEmptyStringTest() {
        // given
        Member member = newMockMember(1L, "test3", "", MemberEnum.CUSTOMER);

        // when
        Boolean hasNickname = memberService.isEmptyMemberNickname(member);

        // then
        assertThat(hasNickname).isTrue();
    }
}
