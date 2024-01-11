package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtProcess {

    @Value("${jwt.subject}")
    private String jwtSubject;

    @Value("${jwt.secret}")
    private String secret;

    public String createAccessToken(PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        return createNewAccessToken(member.getId(), member.getRole().toString());
    }

    // 토큰 생성
    public String createNewAccessToken(Long memberId, String role) {
        String jwtToken = JWT.create()
                .withSubject(jwtSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("id", memberId)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(secret));
        return JwtVO.TOKEN_PREFIX + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
    }

    public String createRefreshToken(String id, String memberId, String role, String ip) {
        String refreshToken = JWT.create()
                .withSubject(jwtSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("id", id)
                .withClaim("memberId", memberId)
                .withClaim("role", role)
                .withClaim("ip", ip)
                .sign(Algorithm.HMAC512(secret));

        return JwtVO.TOKEN_PREFIX + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
    }

    public PrincipalDetails verify(String token) {
        DecodedJWT decodedJWT = isSatisfiedToken(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        Member member = Member.builder().id(id).role(MemberEnum.valueOf(role)).build();

        return new PrincipalDetails(member);
    }

    public DecodedJWT isSatisfiedToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
    }
}
