package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleRespDto.SearchMembersRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.SignUpReqDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.refreshToken.RefreshTokenRedisService;
import kr.ac.kumoh.illdang100.tovalley.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CookieUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CookieUtil.addCookie;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.findMemberByIdOrElseThrowEx;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final ReviewRepository reviewRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
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
                .nickname(nickname)
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

        String memberProfileImg =
                (findMember.getImageFile() != null) ? findMember.getImageFile().getStoreFileUrl() : null;
        return new MemberProfileRespDto(memberProfileImg,
                findMember.getMemberName(),
                findMember.getNickname());
    }

    @Override
    @Transactional
    public void logout(HttpServletResponse response, String refreshTokenId) {
        TokenUtil.deleteRefreshToken(refreshTokenId, refreshTokenRedisRepository);
        expireCookie(response, JwtVO.ACCESS_TOKEN);
        expireCookie(response, JwtVO.REFRESH_TOKEN);
        addCookie(response, ISLOGIN, "false", false);
    }

    /**
     * @param memberId:    사용자 pk
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

    /**
     * 회원 탈퇴
     *
     * @param memberId
     */
    @Override
    @Transactional
    public void deleteMember(Long memberId, String refreshToken) {
        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);

        List<Long> tripScheduleIds = tripScheduleRepository.findTripSchedulesByMemberId(findMember.getId())
                .stream()
                .map(TripSchedule::getId)
                .collect(Collectors.toList());

        // 리뷰 삭제
        reviewRepository.deleteAllByTripScheduleIds(tripScheduleIds);

        // 여행 일정 삭제
        tripScheduleRepository.deleteAllByIds(tripScheduleIds);

        // 사용자 프로필 이미지 삭제
        deleteProfileImage(findMember);

        // 리프레시 토큰 삭제
        TokenUtil.deleteRefreshToken(refreshToken, refreshTokenRedisRepository);

        // 회원 삭제
        memberRepository.delete(findMember);
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

    @Override
    public Slice<SearchMembersRespDto> searchMembers(String nickname, Pageable pageable) {
        return memberRepository.findSliceMembersByNickname(nickname, pageable);
    }

    @Override
    @Transactional
    public void changeMemberRole(Long memberId, MemberEnum role) {

        Member findMember = findMemberByIdOrElseThrowEx(memberRepository, memberId);
        MemberEnum newRole = (role == MemberEnum.ADMIN) ? MemberEnum.CUSTOMER : MemberEnum.ADMIN;
        findMember.changeRole(newRole);
    }

    @Override
    public void deleteRefreshTokenByMemberId(Long memberId) {
        refreshTokenRedisService.deleteRefreshTokenByMemberId(String.valueOf(memberId));
    }

    /**
     * 분실물 게시글 등록 시 작성자 닉네임 유무 검사
     * @param member
     * @return
     */
    @Override
    public Boolean isEmptyMemberNickname(Member member) {

        String nickname = member.getNickname();
        return nickname == null || StringUtils.isBlank(nickname);
    }
}
