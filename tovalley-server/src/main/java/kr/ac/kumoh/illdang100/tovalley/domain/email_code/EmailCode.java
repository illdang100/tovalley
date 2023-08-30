package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import lombok.*;

import javax.persistence.*;

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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EmailCodeStatusEnum emailCodeStatus;

    public void changeEmailCodeStatus(EmailCodeStatusEnum emailCodeStatus) {
        this.emailCodeStatus = emailCodeStatus;
    }

    public void changeVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}