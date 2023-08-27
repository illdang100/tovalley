package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeStatusEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.EmailMessageDto;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.SignUpReqDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.email_code.EmailCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.UUID;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.ISLOGIN;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.addCookie;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final JwtProcess jwtProcess;
    private final EmailCodeService emailCodeService;
    private final EmailCodeRepository emailCodeRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Member signUp(SignUpReqDto signUpReqDto) {
        // 이메일 중복 검사
        String email = signUpReqDto.getEmail();
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new CustomApiException("이미 가입된 회원입니다.");
        });

        // 닉네임 중복 검사
        String nickname = signUpReqDto.getNickname();
        isNicknameAvailable(nickname);

        String password = passwordEncoder.encode(signUpReqDto.getPassword());
        Member signUpMember = Member.builder()
                .memberName(signUpReqDto.getName())
                .username(email)
                .email(email)
                .password(password)
                .role(MemberEnum.CUSTOMER)
                .build();

        return memberRepository.save(signUpMember);
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
    @Transactional
    public void updateMemberNick(Long memberId, String newNickname) {

        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        findMember.changeNickname(newNickname);
    }

    @Override
    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile memberImage) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        processImageUpdate(findMember, memberImage);
    }

    private void processImageUpdate(Member member, MultipartFile memberImage) {
        try {
            if (memberImage != null) {
                ImageFile createdImageFile = s3Service.upload(memberImage, FileRootPathVO.MEMBER_PATH);
                deleteProfileImage(member);
                member.changeImageFile(createdImageFile);
            } else {
                deleteProfileImage(member);
            }
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private void deleteProfileImage(Member member) {
        if (hasAccountProfileImage(member)) {
            try {
                s3Service.delete(member.getImageFile().getStoreFileName());
                member.changeImageFile(null);
            } catch (Exception e) {
                throw new CustomApiException(e.getMessage());
            }
        }
    }

    private boolean hasAccountProfileImage(Member member) {
        return member.getImageFile() != null;
    }
}
