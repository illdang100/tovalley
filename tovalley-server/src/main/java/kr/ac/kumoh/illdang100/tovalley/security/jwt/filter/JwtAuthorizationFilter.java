package kr.ac.kumoh.illdang100.tovalley.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
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

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isCookieVerify(request)) {
            String token = request.getHeader(JwtVO.ACCESS_TOKEN_HEADER).replace(JwtVO.TOKEN_PREFIX, "");

            try {
                PrincipalDetails loginMember = jwtProcess.verify(token);

                // 임시 세션 (인증과 권한 체크용)
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        loginMember, null, loginMember.getAuthorities()
                );

                // 강제 로그인
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                fail(response);
                return;
            }
        }
    }

    private static void fail(HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        ResponseDto<Object> responseDto = new ResponseDto<>(-1, "만료된 토큰입니다.", null);

        String responseBody = om.writeValueAsString(responseDto);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private boolean isCookieVerify(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return false;
        else
            return true;
    }
}
