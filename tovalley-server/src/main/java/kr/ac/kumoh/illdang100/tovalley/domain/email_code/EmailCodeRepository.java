package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface EmailCodeRepository extends CrudRepository<EmailCode, Long> {

    Optional<EmailCode> findByEmail(String email);
}