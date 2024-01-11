package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RefreshTokenRedisRepositoryTest {
    @Autowired
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Test
    @DisplayName("Redis - 리프레시 토큰 repository 테스트")
    void redis_test() throws Exception {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
                .id("testId")
                .role(MemberEnum.CUSTOMER.toString())
                .refreshToken("testRefreshToken")
                .build();

        //when
        refreshTokenRedisRepository.save(refreshToken);

        Optional<RefreshToken> findTokenOpt = refreshTokenRedisRepository.findById("testId");

        RefreshToken findToken = findTokenOpt.get();

        //then
        assertThat(findToken.getId()).isEqualTo("testId");
        assertThat(findToken.getRole()).isEqualTo(MemberEnum.CUSTOMER.toString());
        assertThat(findToken.getRefreshToken()).isEqualTo("testRefreshToken");

    }
}
