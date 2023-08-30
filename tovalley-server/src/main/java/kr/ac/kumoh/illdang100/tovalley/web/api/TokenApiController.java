package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenApiController {

    private final MemberService memberService;

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> reIssueToken(HttpServletRequest request, HttpServletResponse response) {

        memberService.reIssueToken(request, response);

        return new ResponseEntity<>(new ResponseDto<>(1, "토큰 재발급을 성공했습니다.", null), HttpStatus.OK);
    }
}
