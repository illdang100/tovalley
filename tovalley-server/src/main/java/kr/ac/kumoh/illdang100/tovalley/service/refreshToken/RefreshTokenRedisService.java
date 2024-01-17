package kr.ac.kumoh.illdang100.tovalley.service.refreshToken;

import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RefreshTokenRedisService {
    private final RefreshTokenRedisRepository refreshTokenRepository;

    public RefreshTokenRedisService(RefreshTokenRedisRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void deleteRefreshTokenByMemberId(String memberId) {
        Set<RefreshToken> refreshTokens = refreshTokenRepository.findByMemberId(memberId);
        refreshTokenRepository.deleteAll(refreshTokens);
    }
}
