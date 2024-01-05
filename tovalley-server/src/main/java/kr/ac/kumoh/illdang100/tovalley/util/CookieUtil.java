package kr.ac.kumoh.illdang100.tovalley.util;

import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

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

    public static boolean isCookieVerify(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if (cookie.getName().equals(cookieName) && cookieValue.startsWith(JwtVO.TOKEN_PREFIX)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String findCookieValue(HttpServletRequest request, String cookieName) {
        String token = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String cookieValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            if (cookie.getName().equals(cookieName) && cookieValue.startsWith(JwtVO.TOKEN_PREFIX)) {
                token = cookieValue;
            }
        }
        return token;
    }

}
