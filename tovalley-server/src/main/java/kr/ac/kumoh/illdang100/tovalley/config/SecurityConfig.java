package kr.ac.kumoh.illdang100.tovalley.config;

import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.RefreshTokenRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.filter.JwtAuthenticationFilter;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.filter.JwtAuthorizationFilter;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.CustomOAuth2UserService;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.OAuth2FailureHandler;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.OAuth2SuccessHandler;
import kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtProcess jwtProcess;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProcess, refreshTokenRedisRepository));
            http.addFilter(new JwtAuthorizationFilter(authenticationManager, jwtProcess));
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); // iframe 허용안함
        http.csrf().disable(); // csrf 허용안함

        http.cors().configurationSource(configurationSource());

        // 인증 실패 처리
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        });
        // 권한 실패
        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
        });

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();

        // 필터 적용
        http.apply(new CustomSecurityFilterManager());

        http
                .authorizeHttpRequests()
                .antMatchers("/api/auth/**").authenticated()
                .antMatchers("/admin/**").hasRole("" + MemberEnum.ADMIN)
                .anyRequest().permitAll();

        http
                .oauth2Login().loginPage("/token/expired")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint().userService(oAuth2UserService);

        return http.build();
    }

    /**
     * CORS
     */
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, DELETE, PUT
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 엔드 ip만 허용하도록 할 수도 있다.)
        configuration.setAllowCredentials(true); // 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
