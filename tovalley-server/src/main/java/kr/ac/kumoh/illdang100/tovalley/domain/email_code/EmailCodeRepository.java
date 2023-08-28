package kr.ac.kumoh.illdang100.tovalley.domain.email_code;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.email_code.EmailCode;


import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

    Optional<EmailCode> findByEmail(String email);
}