package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.service.email_code.EmailCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.email_code.EmailCodeReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.email_code.EmailCodeRespDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email-code")
public class EmailCodeController {

    private final EmailCodeService emailCodeService;

    @PostMapping
    public ResponseEntity<?> sendEmail (@RequestBody @Valid SendEmailCodeReqDto sendEmailCodeReqDto,
                                        BindingResult bindingResult) {
        SendEmailCodeRespDto sendEmailCodeRespDto = emailCodeService.sendEmail(sendEmailCodeReqDto.getEmail());

        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 이메일로 인증 코드를 전송했습니다.", sendEmailCodeRespDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getVerifyCode(@RequestParam String email, @RequestParam String verifyCode) {
        emailCodeService.checkVerifyCode(email, verifyCode);

        return new ResponseEntity<>(new ResponseDto<>(1, "이메일 인증을 성공했습니다.", null), HttpStatus.OK);
    }
}
