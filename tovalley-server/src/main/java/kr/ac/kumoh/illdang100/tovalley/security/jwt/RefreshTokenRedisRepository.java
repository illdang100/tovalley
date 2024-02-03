package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    Set<RefreshToken> findByMemberId(String memberId);
}
