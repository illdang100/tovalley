package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.SignUpReqDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.*;
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

    /**
     * @param nickname: 변경하고 싶은 닉네임
     * @methodnme: isNicknameAvailable
     * @author: JYeonJun
     * @description: 닉네임 사용 가능 여부 확인
     * @return:
     */
    @Override
    public void isNicknameAvailable(String nickname) {

        Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

        memberOptional.ifPresent((findMember) -> {
            throw new CustomApiException("사용 불가능한 닉네임입니다.");
        });
    }

    @Override
    public String findSignedUpEmail(FindEmailReqDto findEmailReqDto) {
        String email = findEmailReqDto.getEmail();
        Member findMember = findMemberByEmailOrElseThrowEx(memberRepository, email);

        return findMember.getEmail();
}

    @Override
    @Transactional
    public void resetPassword(ResetPasswordReqDto resetPasswordReqDto) {
        String email = resetPasswordReqDto.getEmail();
        String newPassword = resetPasswordReqDto.getNewPassword();
        String encodedPw = passwordEncoder.encode(newPassword);

        Member findMember = findMemberByEmailOrElseThrowEx(memberRepository, email);
        findMember.changePassword(encodedPw);
    }

    @Override
    @Transactional
    public void reIssueToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (isCookieVerify(request)) {
            String refreshToken = findCookieValue(request, JwtVO.REFRESH_TOKEN);
            String jwtToken = refreshToken.replace(JwtVO.TOKEN_PREFIX, "");

            // 리프레시 토큰 유효성 검사
            try {
                jwtProcess.isSatisfiedToken(jwtToken);
            } catch (Exception e) {
                throw new CustomApiException("유효하지 않은 토큰입니다");
            }

            RefreshToken findRefreshToken
                    = findRefreshTokenOrElseThrowEx(refreshTokenRedisRepository, refreshToken);

            // 토큰 재발급
            String memberId = findRefreshToken.getId();
            String memberRole = findRefreshToken.getRole();

            String newAccessToken = jwtProcess.createNewAccessToken(Long.valueOf(memberId), memberRole);
            String newRefreshToken = jwtProcess.createRefreshToken(memberId, memberRole);

            findRefreshToken.changeRefreshToken(newRefreshToken);
            refreshTokenRedisRepository.save(findRefreshToken);

            // 토큰을 쿠키에 추가
            addCookie(response, JwtVO.ACCESS_TOKEN, newAccessToken);
            addCookie(response, JwtVO.REFRESH_TOKEN, newRefreshToken);
        }
    }

    /**
     * @param memberId: 사용자 pk
     * @methodnme: getMemberDetail
     * @author: JYeonJun
     * @description: 사용자 개인정보 조회
     * @return: 개인정보 dto
     */
    @Override
    public MemberProfileRespDto getMemberDetail(Long memberId) {

        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);

        String memberProfileImg = (findMember.getImageFile() != null) ? findMember.getImageFile().getStoreFileUrl() : null;
        return new MemberProfileRespDto(memberProfileImg,
                findMember.getMemberName(),
                findMember.getNickname());
    }

    @Override
    @Transactional
    public void logout(HttpServletResponse response, String refreshToken) {
        deleteRefreshToken(refreshToken);
        expireCookie(response, JwtVO.ACCESS_TOKEN);
        expireCookie(response, JwtVO.REFRESH_TOKEN);
        addCookie(response, ISLOGIN, "false", false);
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void deleteRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRedisRepository.findByRefreshToken(refreshToken);
        if (refreshTokenOpt.isPresent()) {
            RefreshToken findRefreshToken = refreshTokenOpt.get();
            refreshTokenRedisRepository.delete(findRefreshToken);
        }
    }

    /**
     * @param memberId: 사용자 pk
     * @param newNickname: 변경할 새로운 닉네임
     * @methodnme: updateMemberNick
     * @author: JYeonJun
     * @description: 사용자 닉네임 변경
     * @return:
     */
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
