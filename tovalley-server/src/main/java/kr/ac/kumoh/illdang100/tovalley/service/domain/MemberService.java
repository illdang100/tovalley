package kr.ac.kumoh.illdang100.tovalley.service.domain;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto.*;

/**
 * 회원 관리
 */
public interface MemberService {

    // 회원가입
    Member signUp(SignUpReqDto signUpReqDto);

    // 닉네임 중복 검사
    void isNicknameAvailable(String nickname);

    // 서비스 자체 로그인
    Member login(LoginReqDto loginReqDto);

    // 아이디 찾기
    String findIdByEmail(String email);

    // 패스워드 찾기 - 인증 이메일 발송
    void sendPasswordResetEmail(String email);

    // 새로운 패스워드 설정
    void resetPassword(String email, String newPassword);

    // 토큰 재발급
    void reIssueToken(HttpServletResponse response, String refreshToken);

    // 사용자 정보 조회
    MemberDetailRespDto getMemberDetail(Long memberId);

    // 사용자 로그아웃 - 리프래시 토큰 삭제
    void logout();

    // 사용자 닉네임 업데이트
    void updateMemberNick(Long memberId, String newNickname);

    // 사용자 프로필 이미지 업데이트
    void updateMemberImage(Long memberId, MultipartFile memberImage);

    // 기본 프로필 이미지로 변경
    void setDefaultProfileImage(Long memberId);
}
