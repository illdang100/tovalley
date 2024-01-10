package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleReqDto;
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
@RequestMapping("/th")
@Slf4j
public class AdminController {
    private final MemberService memberService;

    /**
     * @param model
     * @return 관리자용 로그인 페이지
     */
    @GetMapping("/admin-login")
    public String loginForm(Model model) {

        model.addAttribute("loginForm", new LoginForm());
        return "admin/admin-login";
    }

    /**
     * 관리자 로그아웃
     * @return 관리자용 로그인 페이지
     */
    @GetMapping("/logout")
    public String logOut(@CookieValue(JwtVO.REFRESH_TOKEN) String refreshToken,
                         HttpServletResponse response) {
        memberService.logout(response, refreshToken);

        return "redirect:/th/admin-login";
    }

    /**
     * 사용자 권한 변경 페이지
     * @param model
     * @return
     */
    @GetMapping("/change-role")
    public String changeRoleForm(Model model) {
        return "admin/change-role";
    }

    /**
     * 사용자 권한 변경
     * @param adminChangeRoleReqDto
     * @param refreshTokenId
     * @param nmodel
     * @return
     */
    @PostMapping("/change-role")
    public String changeRole(AdminChangeRoleReqDto adminChangeRoleReqDto, @CookieValue(JwtVO.REFRESH_TOKEN) String refreshTokenId, Model nmodel) {
        return "admin/change-role";
    }

    /**
     * 사용자 검색 - 닉네임
     * @param nickname
     * @param model
     * @return
     */
    @PostMapping("/members")
    public String getMemberList(String nickname, Model model) {
        return "admin/change-role";
    }

}
