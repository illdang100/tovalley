package kr.ac.kumoh.illdang100.tovalley.service.email_code;


import static kr.ac.kumoh.illdang100.tovalley.dto.email_code.EmailCodeRespDto.*;

public interface EmailCodeService {
    public SendEmailCodeRespDto sendEmail(String email);

    public void checkVerifyCode(String email, String verifyCode);
}
