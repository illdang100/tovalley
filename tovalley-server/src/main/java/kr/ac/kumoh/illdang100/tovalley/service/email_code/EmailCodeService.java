package kr.ac.kumoh.illdang100.tovalley.service.email_code;

import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;

import static kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.*;

public interface EmailCodeService {
    public EmailCode sendEmail(EmailMessageDto messageDto, String type);
}
