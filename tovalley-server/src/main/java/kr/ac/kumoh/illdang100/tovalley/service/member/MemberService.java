package kr.ac.kumoh.illdang100.tovalley.service.member;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    // 아이디 찾기
    String findSignedUpEmail(FindEmailReqDto findEmailReqDto);

    // 새로운 패스워드 설정
    void resetPassword(ResetPasswordReqDto resetPasswordReqDto);

    // 토큰 재발급
    void reIssueToken(HttpServletRequest request, HttpServletResponse response);

    // 사용자 정보 조회
    MemberProfileRespDto getMemberDetail(Long memberId);

    // 사용자 로그아웃 - 리프래시 토큰 삭제
    void logout(HttpServletResponse response, String refreshToken);

    // 사용자 닉네임 업데이트
    void updateMemberNick(Long memberId, String newNickname);

    // 사용자 프로필 이미지 업데이트
    void updateProfileImage(Long memberId, MultipartFile memberImage);
}
