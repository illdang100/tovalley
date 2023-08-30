package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCode {

    @Id
    @Column(name = "email_code_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    private String email;

    @Column(nullable = false, length = 7)
    private String verifyCode;

    @Column(nullable = false)
    private EmailCodeStatusEnum emailCodeStatus;

    public void updateEmailCodeStatus(EmailCodeStatusEnum emailCodeStatus) {
        this.emailCodeStatus = emailCodeStatus;
    }
}