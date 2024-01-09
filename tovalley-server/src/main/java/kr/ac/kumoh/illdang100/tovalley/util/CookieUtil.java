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
                if (cookie.getName().equals(cookieName)) {
                    // access token이나 refresh token이라면 검증에 성공했다고 판단
                    if (cookieName.equals(JwtVO.ACCESS_TOKEN) && cookieValue.startsWith(JwtVO.TOKEN_PREFIX)) {
                        return true;
                    }
                    // refreshTokenId는 특정 prefix 없이 바로 검증에 성공했다고 판단
                    else if (cookieName.equals(JwtVO.REFRESH_TOKEN)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String findCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if (cookie.getName().equals(cookieName)) {
                    // access token이라면 TOKEN_PREFIX를 제거한 후 반환
                    if (cookieName.equals(JwtVO.ACCESS_TOKEN) && cookieValue.startsWith(JwtVO.TOKEN_PREFIX)) {
                        return cookieValue.replace(JwtVO.TOKEN_PREFIX, "");
                    }
                    // refreshTokenId라면 바로 반환
                    else if (cookieName.equals(JwtVO.REFRESH_TOKEN)) {
                        return cookieValue;
                    }
                }
            }
        }
        return "";
    }
}
