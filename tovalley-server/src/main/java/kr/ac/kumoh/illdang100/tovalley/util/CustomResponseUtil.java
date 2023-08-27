package kr.ac.kumoh.illdang100.tovalley.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 성공", dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<String> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
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
}

