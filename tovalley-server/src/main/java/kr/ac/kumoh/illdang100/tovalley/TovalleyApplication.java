package kr.ac.kumoh.illdang100.tovalley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class TovalleyApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}
	public static void main(String[] args) {
		SpringApplication.run(TovalleyApplication.class, args);
	}

	@Bean
	public InMemoryHttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}
}
