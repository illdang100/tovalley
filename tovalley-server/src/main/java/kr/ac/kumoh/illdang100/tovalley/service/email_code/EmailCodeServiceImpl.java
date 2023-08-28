package kr.ac.kumoh.illdang100.tovalley.service.email_code;

import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCodeRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService{
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailCodeRepository emailCodeRepository;

    @Override
    public EmailCode sendEmail(MemberReqDto.EmailMessageDto messageDto, String type)  {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String verifyStr = generateRandomString();
        MimeMessageHelper mimeMessageHelper = null;
        String email = "";
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            email = messageDto.getTo();
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(messageDto.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(verifyStr, type), true); // 메일 본문 내용, HTML 여부

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return emailCodeRepository.save(EmailCode.builder()
                .email(email)
                .verifyCode(verifyStr)
                .build());
    }

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

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
}
