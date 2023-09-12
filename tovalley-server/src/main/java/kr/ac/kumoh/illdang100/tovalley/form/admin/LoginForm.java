package kr.ac.kumoh.illdang100.tovalley.form.admin;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 관리자용 로그인 Form
 */
@Data
public class LoginForm {

    @NotEmpty(message = "이메일은 필수입니다")
    private String username;

    @NotEmpty(message = "패스워드는 필수입니다")
    private String password;
}