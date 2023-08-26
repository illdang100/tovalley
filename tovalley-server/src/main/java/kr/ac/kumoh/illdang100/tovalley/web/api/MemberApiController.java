package kr.ac.kumoh.illdang100.tovalley.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "Member", description = "사용자 API Document")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/members/check-nickname")
    @Operation(summary = "닉네임 중복검사", description = "닉네임 중복검사 결과를 출력합니다.")
    public ResponseEntity<?> checkNickname(@RequestBody @Valid ValidateNicknameRequest validateNicknameRequest,
                                                    BindingResult bindingResult) {

        memberService.isNicknameAvailable(validateNicknameRequest.getNickname());
        return new ResponseEntity<>(new ResponseDto<>(1, "사용 가능한 닉네임입니다", null), HttpStatus.OK);
    }

    @PostMapping("/auth/members/set-nickname")
    @Operation(summary = "사용자 닉네임 변경", description = "사용자 닉네임 변경 결과를 출력합니다.")
    public ResponseEntity<?> updateNickname(@RequestBody @Valid ValidateNicknameRequest validateNicknameRequest,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        memberService.updateMemberNick(principalDetails.getMember().getId(), validateNicknameRequest.getNickname());
        return new ResponseEntity<>(new ResponseDto<>(1, "닉네임 변경에 성공하였습니다", null), HttpStatus.OK);
    }

    @PostMapping(value = "/members/profile-image")
    @Operation(summary = "사용자 프로필 이미지 설정", description = "사용자의 프로필 이미지를 설정합니다.")
    public ResponseEntity<?> changeProfileImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @RequestPart(value = "image", required = false) MultipartFile memberImage) { // input 태그의 name 속성과 값이 동일해야 한다.

        // 만약 memberImage가 null이라면 기본 이미지로 변경, 아니라면 해당 이미지로 변경
        memberService.updateProfileImage(principalDetails.getMember().getId(), memberImage);

        return new ResponseEntity<>(new ResponseDto<>(1, "이미지가 성공적으로 변경되었습니다", null), HttpStatus.OK);
    }
}
