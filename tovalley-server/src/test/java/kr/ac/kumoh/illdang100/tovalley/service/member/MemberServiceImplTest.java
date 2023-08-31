package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest extends DummyObject {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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

}
