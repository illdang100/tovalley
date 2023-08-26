package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminHomeController {

    /**
     * @param model
     * @return 관리자용 홈화면(지역별 사건사고 발생 현황 관리)
     */
    @GetMapping("/home")
    public String adminHome(Model model) {

        return "admin/home";
    }
}
