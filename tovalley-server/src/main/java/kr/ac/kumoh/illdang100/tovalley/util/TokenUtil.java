package kr.ac.kumoh.illdang100.tovalley.util;

import java.util.UUID;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;

public class TokenUtil {
    public static String saveRefreshToken(JwtProcess jwtProcess,
                                          RefreshTokenRedisRepository refreshTokenRedisRepository,
                                          String memberId,
                                          String memberRole,
                                          String ip) {

        String refreshTokenId = UUID.randomUUID().toString();

        String refreshToken = jwtProcess.createRefreshToken(refreshTokenId, memberId, memberRole, ip);

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(refreshTokenId)
                .memberId(memberId)
                .role(memberRole)
                .ip(ip)
                .refreshToken(refreshToken)
                .build());

        return refreshTokenId;
    }
}
