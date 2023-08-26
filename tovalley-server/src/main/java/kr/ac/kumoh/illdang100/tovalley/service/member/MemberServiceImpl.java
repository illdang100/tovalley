package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.util.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

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

        Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

        memberOptional.ifPresent((findMember) -> {
            throw new CustomApiException("사용 불가능한 닉네임입니다.");
        });
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

        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);

        String memberProfileImg = (findMember.getImageFile() != null) ? findMember.getImageFile().getStoreFileUrl() : null;
        return new MemberProfileRespDto(memberProfileImg,
                findMember.getMemberName(),
                findMember.getNickname());
    }

    @Override
    public void logout() {

    }

    @Override
    public void updateMemberNick(Long memberId, String newNickname) {

        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        findMember.changeNickname(newNickname);
    }

    @Override
    public void updateMemberImage(Long memberId, MultipartFile memberImage) {

    }

    @Override
    public void setDefaultProfileImage(Long memberId) {

    }
}
