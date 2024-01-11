package kr.ac.kumoh.illdang100.tovalley.security.jwt;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
