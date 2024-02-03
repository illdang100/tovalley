package kr.ac.kumoh.illdang100.tovalley.util;

import java.util.UUID;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;

import java.util.Optional;

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

    public static void deleteRefreshToken(String refreshTokenId, RefreshTokenRedisRepository refreshTokenRedisRepository) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRedisRepository.findById(refreshTokenId);
        if (refreshTokenOpt.isPresent()) {
            RefreshToken findRefreshToken = refreshTokenOpt.get();
            refreshTokenRedisRepository.delete(findRefreshToken);
        }
    }

    public static String createAccessToken_test(MemberRepository memberRepository, JwtProcess jwtProcess, String username) {
        Member member = memberRepository.findByUsername(username).get();
        PrincipalDetails loginUser = new PrincipalDetails(member);
        return jwtProcess.createAccessToken(loginUser);
    }
}
