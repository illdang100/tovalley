package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member signUp(MemberReqDto.SignUpReqDto signUpReqDto) {
        return null;
    }

    @Override
    public void isNicknameAvailable(String nickname) {

    }

    @Override
    public Member login(MemberReqDto.LoginReqDto loginReqDto) {
        return null;
    }

    @Override
    public String findIdByEmail(String email) {
        return null;
    }

    @Override
    public void sendPasswordResetEmail(String email) {

    }

    @Override
    public void resetPassword(String email, String newPassword) {

    }

    @Override
    public void reIssueToken(HttpServletResponse response, String refreshToken) {

    }

    @Override
    public MemberProfileRespDto getMemberDetail(Long memberId) {

        Member findMember = findMemberOrElseThrowEx(memberId);

        String memberProfileImg = (findMember.getImageFile() != null) ? findMember.getImageFile().getStoreFileUrl() : null;
        return new MemberProfileRespDto(memberProfileImg,
                findMember.getMemberName(),
                findMember.getNickname());
    }

    private Member findMemberOrElseThrowEx(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다."));
    }

    @Override
    public void logout() {

    }

    @Override
    public void updateMemberNick(Long memberId, String newNickname) {

    }

    @Override
    public void updateMemberImage(Long memberId, MultipartFile memberImage) {

    }

    @Override
    public void setDefaultProfileImage(Long memberId) {

    }
}
