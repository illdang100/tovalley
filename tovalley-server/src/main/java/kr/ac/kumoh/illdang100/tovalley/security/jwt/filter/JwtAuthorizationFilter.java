package kr.ac.kumoh.illdang100.tovalley.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.addCookie;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.findRefreshTokenOrElseThrowEx;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;

    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess, RefreshTokenRedisRepository refreshTokenRedisRepository) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestUrl = request.getRequestURL().toString();
        boolean containsApiAuth = requestUrl.contains("/api/auth/");
        boolean containsAdmin = requestUrl.contains("/th/admin/");
        if ((containsApiAuth || containsAdmin) && isCookieVerify(request, JwtVO.ACCESS_TOKEN)) {
            String token = findCookieValue(request, JwtVO.ACCESS_TOKEN).replace(JwtVO.TOKEN_PREFIX, "");
            log.debug("accessToken={}", token);

            try {
                PrincipalDetails loginMember = jwtProcess.verify(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        loginMember, null, loginMember.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                reIssueToken(request, response);
            }
        }
        chain.doFilter(request, response);
    }

    private void reIssueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isCookieVerify(request, JwtVO.REFRESH_TOKEN)) {
            String refreshToken = findCookieValue(request, JwtVO.REFRESH_TOKEN);
            String jwtToken = refreshToken.replace(JwtVO.TOKEN_PREFIX, "");

            // 리프레시 토큰 유효성 검사
            try {
                jwtProcess.isSatisfiedToken(jwtToken);
            } catch (Exception e) {
                handleTokenVerificationFailure(response);
                return;
            }

            RefreshToken findRefreshToken
                    = findRefreshTokenOrElseThrowEx(refreshTokenRedisRepository, refreshToken);

            // 토큰 재발급
            String memberId = findRefreshToken.getId();
            String memberRole = findRefreshToken.getRole();

            String newAccessToken = jwtProcess.createNewAccessToken(Long.valueOf(memberId), memberRole);
            log.debug("[토큰 재발급]accessToken={}", newAccessToken);

            // 토큰을 쿠키에 추가
            addCookie(response, JwtVO.ACCESS_TOKEN, newAccessToken);
        }
    }

    private void handleTokenVerificationFailure(HttpServletResponse response) throws IOException {
        addCookie(response, ISLOGIN, "false");

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDto<Object> responseDto = new ResponseDto<>(-1, "만료된 토큰입니다.", null);
        String responseBody = objectMapper.writeValueAsString(responseDto);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
