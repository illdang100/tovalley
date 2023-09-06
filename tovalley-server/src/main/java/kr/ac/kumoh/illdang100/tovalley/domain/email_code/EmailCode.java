package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "emailCode", timeToLive = 210)
public class EmailCode {

    @Id
    @Indexed
    private String email;

    private String verifyCode;
}