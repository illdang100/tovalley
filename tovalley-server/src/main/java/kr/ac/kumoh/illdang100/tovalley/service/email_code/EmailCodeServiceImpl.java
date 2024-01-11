package kr.ac.kumoh.illdang100.tovalley.service.email_code;

import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

import static kr.ac.kumoh.illdang100.tovalley.dto.email_code.EmailCodeRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Service
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {
    private final JavaMailSender javaMailSender;
    private final EmailCodeRepository emailCodeRepository;

    @Override
    @Transactional
    public SendEmailCodeRespDto sendEmailVerification(String email) {

        EmailCode findEmailCode = emailCodeRepository.findByEmail(email).orElse(null);
        if (findEmailCode != null)
            throw new CustomApiException("이메일 수신함을 확인하세요");

        String verifyCode = sendVerifyCodeByEmail(email);
        emailCodeRepository.save(EmailCode.builder()
                .email(email)
                .verifyCode(verifyCode)
                .build());

        return new SendEmailCodeRespDto(verifyCode);
    }

    private String sendVerifyCodeByEmail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String verifyCode = generateRandomString();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("이메일 인증 코드"); // 메일 제목
            mimeMessageHelper.setText("인증 코드를 입력하세요: " + verifyCode); // 메일 본문 내용

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomApiException("이메일 전송을 실패했습니다");
        }
        return verifyCode;
    }

    @Override
    @Transactional
    public void checkVerifyCode(String email, String verifyCode) {
        EmailCode findEmailCode = findEmailCodeByEmailOrElseThrowEx(emailCodeRepository, email);

        if (!isEqualsVerifyCode(verifyCode, findEmailCode))
            throw new CustomApiException("이메일 인증에 실패했습니다");
    }

    private boolean isEqualsVerifyCode(String verifyCode, EmailCode findEmailCode) {
        return findEmailCode.getVerifyCode().equals(verifyCode);
    }


    // 인증 코드 생성 (7글자)
    private static String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randomString = new StringBuilder();
        int length = 7;

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    @Override
    public void sendReLoginRequestEmail(String email) {

    }
}
