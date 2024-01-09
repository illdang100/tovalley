package kr.ac.kumoh.illdang100.tovalley.security.jwt;

public interface JwtVO {
    // TODO: 만료시간 30분에서 1분
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 1; // 30분
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14; // 2주
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "ACCESSTOKEN";
    public static final String REFRESH_TOKEN = "REFRESHTOKENID";
}
