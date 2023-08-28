package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.form.admin.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    /**
     * @param model
     * @return 관리자용 로그인 페이지
     */
    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("loginForm", new LoginForm());
        return "admin/loginForm";
    }
}
