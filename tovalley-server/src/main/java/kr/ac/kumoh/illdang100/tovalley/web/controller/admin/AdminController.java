package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import javax.validation.Valid;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleReqDto.ChangeMemberRoleReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleReqDto.SearchMembersCondition;
import kr.ac.kumoh.illdang100.tovalley.form.admin.LoginForm;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.service.email_code.EmailCodeService;
import kr.ac.kumoh.illdang100.tovalley.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleRespDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/th")
@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final EmailCodeService emailCodeService;

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
     *
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
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/change-role")
    public String changeRoleForm(@PageableDefault(size = 10, sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable,
                                 Model model) {

        model.addAttribute("searchMembers", getSearchMembers(null, pageable));
        model.addAttribute("searchMembersCondition", new SearchMembersCondition());
        model.addAttribute("changeMemberRoleReqDto", new ChangeMemberRoleReqDto());
        return "admin/change-role";
    }

    /**
     * 사용자 권한 변경
     *
     * @param changeMemberRoleReqDto
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/change-role")
    public String changeRole(@ModelAttribute @Valid ChangeMemberRoleReqDto changeMemberRoleReqDto,
                             BindingResult result,
                             Model model) {
        boolean isSuccess = true;
        if (result.hasErrors()) {
            isSuccess = false;
            model.addAttribute("isSuccess", isSuccess);
            return handleErrors(model);
        }

        executeRoleChange(changeMemberRoleReqDto);
        model.addAttribute("isSuccess", isSuccess);
        return changeRoleForm(PageRequest.of(0, 10, Sort.by("nickname").ascending()), model);
    }

    private String handleErrors(Model model) {
        model.addAttribute("searchMembers", getSearchMembers(null, defaultPageable()));
        return "admin/change-role";
    }

    private PageRequest defaultPageable() {
        return PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nickname"));
    }

    private Slice<SearchMembersRespDto> getSearchMembers(String nickname, Pageable pageable) {
        return memberService.searchMembers(nickname, pageable);
    }

    private void executeRoleChange(ChangeMemberRoleReqDto changeMemberRoleReqDto) {
        Long memberId = changeMemberRoleReqDto.getMemberId();
        String email = changeMemberRoleReqDto.getEmail();
        MemberEnum role = MemberEnum.valueOf(changeMemberRoleReqDto.getRole());

        memberService.changeMemberRole(memberId, role);
        memberService.deleteRefreshTokenByMemberId(memberId);
        emailCodeService.sendReLoginRequestEmail(email);
    }

    /**
     * 사용자 검색 - 닉네임
     *
     * @param searchMembersCondition
     * @param result
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/members")
    public String getMemberList(@ModelAttribute @Valid SearchMembersCondition searchMembersCondition,
                                BindingResult result,
                                @PageableDefault(size = 10, sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable,
                                Model model) {

        boolean isSuccess = true;
        if (result.hasErrors()) {
            isSuccess = false;
            model.addAttribute("isSuccess", isSuccess);
            return handleErrors(model);
        }

        model.addAttribute("searchMembers", getSearchMembers(searchMembersCondition.getNickname(), pageable));
        model.addAttribute("isSuccess", isSuccess);
        return "admin/change-role";
    }
}
