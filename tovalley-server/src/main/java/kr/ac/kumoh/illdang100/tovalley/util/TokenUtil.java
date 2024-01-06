package kr.ac.kumoh.illdang100.tovalley.util;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;

public class TokenUtil {
    public static String saveRefreshToken(JwtProcess jwtProcess, RefreshTokenRedisRepository refreshTokenRedisRepository, Member member, String ip) {
        String memberId = member.getId().toString();
        String role = member.getRole().toString();

        String refreshToken = jwtProcess.createRefreshToken(memberId, role, ip);

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(memberId)
                .role(role)
                .ip(ip)
                .refreshToken(refreshToken)
                .build());

        return refreshToken;
    }
}
