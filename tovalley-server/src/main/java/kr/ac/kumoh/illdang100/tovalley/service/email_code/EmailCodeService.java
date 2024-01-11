package kr.ac.kumoh.illdang100.tovalley.service.email_code;


import static kr.ac.kumoh.illdang100.tovalley.dto.email_code.EmailCodeRespDto.*;

public interface EmailCodeService {
    SendEmailCodeRespDto sendEmailVerification(String email);

    void checkVerifyCode(String email, String verifyCode);

    void sendReLoginRequestEmail(String email);
}
