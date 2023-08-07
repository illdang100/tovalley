package kr.ac.kumoh.illdang100.tovalley.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject {

    private final BCryptPasswordEncoder passwordEncoder;

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner devInit() {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

        };
    }

    @Profile("prod")
    @Bean
    CommandLineRunner prodInit() {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

        };
    }
}
