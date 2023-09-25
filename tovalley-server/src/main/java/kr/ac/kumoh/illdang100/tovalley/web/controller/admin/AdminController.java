package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.form.admin.LoginForm;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class AdminController {
    private final MemberService memberService;

    /**
     * @param model
     * @return 관리자용 로그인 페이지
     */
    @GetMapping("/th/admin-login")
    public String loginForm(Model model) {

        model.addAttribute("loginForm", new LoginForm());
        return "admin/admin-login";
    }

    /**
     * 관리자 로그아웃
     * @return 관리자용 로그인 페이지
     */
    @GetMapping("/th/admin/logout")
    public String logOut(@CookieValue(JwtVO.REFRESH_TOKEN) String refreshToken,
                         HttpServletResponse response) {
        memberService.logout(response, refreshToken);

        return "redirect:/th/admin-login";
    }}
