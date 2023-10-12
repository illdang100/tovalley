package kr.ac.kumoh.illdang100.tovalley.security.oauth;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.ISLOGIN;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.addCookie;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.saveRefreshToken;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.redirectUrl}")
    private String redirectUrl;

    private final JwtProcess jwtProcess;

    private  final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        log.info("OAuth2SuccessHandler.onAuthenticationSuccess() Start!!");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Member member = principalDetails.getMember();

        String accessToken = jwtProcess.createAccessToken(principalDetails);

        String refreshToken = saveRefreshToken(jwtProcess, refreshTokenRedisRepository, member);

        log.info("-- Access Token 추가={}", accessToken);
        addCookie(response, JwtVO.ACCESS_TOKEN, accessToken);
        log.info("-- Refresh Token 추가={}", refreshToken);
        addCookie(response, JwtVO.REFRESH_TOKEN, refreshToken);
        log.info("-- ISLOGIN 추가={}", true);
        addCookie(response, ISLOGIN, "true", false);

        log.info("redirectUrl={}", redirectUrl);
        log.info("OAuth2SuccessHandler.onAuthenticationSuccess() End!!");
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
