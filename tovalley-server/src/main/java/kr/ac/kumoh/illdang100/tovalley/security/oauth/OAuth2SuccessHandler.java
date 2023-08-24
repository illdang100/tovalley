package kr.ac.kumoh.illdang100.tovalley.security.oauth;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProcess jwtProcess;

    private  final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private static final String REDIRECT_URL = "http://localhost:3000";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Member member = principalDetails.getMember();

        String accessToken = jwtProcess.createAccessToken(principalDetails);

        String refreshToken = saveRefreshToken(member);

        addCookie(response, "accessToken", accessToken);
        addCookie(response, "refreshToken", refreshToken);
        addCookie(response, "ISLOGIN", "true", false);

        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);
    }

    public static void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, true);
    }

    public static void addCookie(HttpServletResponse response, String name, Long value) {
        addCookie(response, name, String.valueOf(value), true);
    }


    private String saveRefreshToken(Member member) {
        String memberId = member.getId().toString();
        String role = member.getRole().toString();
        String refreshToken = jwtProcess.createRefreshToken(memberId, role);

        log.debug("refreshToken={}", refreshToken);

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(memberId)
                .role(role)
                .refreshToken(refreshToken)
                .build());

        return refreshToken;
    }
}
