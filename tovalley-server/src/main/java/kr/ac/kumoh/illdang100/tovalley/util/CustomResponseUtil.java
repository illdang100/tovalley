package kr.ac.kumoh.illdang100.tovalley.util;

import static kr.ac.kumoh.illdang100.tovalley.util.CookieUtil.addCookie;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);
    public static final String ISLOGIN = "ISLOGIN";

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

    public static void handleTokenVerificationFailure(HttpServletResponse response) throws IOException {
        addCookie(response, ISLOGIN, "false");
        CustomResponseUtil.fail(response, "만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
    }
}

