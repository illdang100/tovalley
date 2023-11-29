package kr.ac.kumoh.illdang100.tovalley.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberReqDto.LoginReqDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JwtProcess jwtProcess;

    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess, RefreshTokenRedisRepository refreshTokenRedisRepository) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
        this.jwtProcess = jwtProcess;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

            // 강제 로그인 (인증 토큰 생성)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(), loginReqDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication; // return 후에는 successfulAuthentication() 호출
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtProcess.createAccessToken(principalDetails);

        String refreshToken = saveRefreshToken(jwtProcess, refreshTokenRedisRepository, principalDetails.getMember());
        addCookie(response, JwtVO.ACCESS_TOKEN, accessToken);
        addCookie(response, JwtVO.REFRESH_TOKEN, refreshToken);
        addCookie(response, ISLOGIN, "true", false);

        CustomResponseUtil.success(response, null);
    }
}
