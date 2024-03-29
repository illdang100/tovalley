package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 1209600)
public class RefreshToken {
    @Id
    private String id;
    @Indexed
    private String memberId;
    private String role;
    private String ip;
    private String refreshToken;
}
