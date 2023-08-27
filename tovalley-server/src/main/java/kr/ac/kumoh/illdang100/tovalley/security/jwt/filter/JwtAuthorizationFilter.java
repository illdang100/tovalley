package kr.ac.kumoh.illdang100.tovalley.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isCookieVerify(request)) {
            String token = findCookieValue(request);
            log.debug("token={}", token);

            try {
                PrincipalDetails loginMember = jwtProcess.verify(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        loginMember, null, loginMember.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                handleTokenVerificationFailure(response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isCookieVerify(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtVO.ACCESS_TOKEN) && cookie.getValue().startsWith(JwtVO.TOKEN_PREFIX)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String findCookieValue(HttpServletRequest request) {
        String token = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtVO.ACCESS_TOKEN) && cookie.getValue().startsWith(JwtVO.TOKEN_PREFIX)) {
                token = cookie.getValue().replace(JwtVO.TOKEN_PREFIX, "");
            }
        }
        return token;
    }

    private void handleTokenVerificationFailure(HttpServletResponse response) throws IOException {
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
