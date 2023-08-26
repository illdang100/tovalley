package kr.ac.kumoh.illdang100.tovalley.security.oauth;

import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.provider.GoogleUserInfo;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.provider.KakaoUserInfo;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.provider.NaverUserInfo;
import kr.ac.kumoh.illdang100.tovalley.security.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return  processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String username = getUsername(oAuth2User, registrationId, attributes);
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        Member member = memberRepository.findByUsername(username)
                .orElseGet(() -> createNewMember(username, oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName()));

        return new PrincipalDetails(member, attributes);
    }

    private String getUsername(OAuth2User oAuth2User, String registrationId, Map<String, Object> attributes) {
        String providerId;
        if (registrationId.equals("google")) {
            log.debug("구글 로그인 요청");
            providerId = oAuth2User.getAttribute("sub");
        }
        else if (registrationId.equals("kakao")) {
            log.debug("카카오 로그인 요청");
            providerId = String.valueOf(attributes.get("id"));
        }
        else if (registrationId.equals("naver")) {
            log.debug("네이버 로그인 요청");
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            providerId = String.valueOf(response.get("id"));
        }
        else {
            log.error("지원하지 않는 소셜 로그인");
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }
        return registrationId + "_" + providerId;
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return new GoogleUserInfo(attributes);
        }
        else if (registrationId.equals("kakao")) {
            return new KakaoUserInfo(attributes);
        }
        else if (registrationId.equals("naver")) {
            return new NaverUserInfo(attributes);
        }
        else {
            log.error("지원하지 않는 소셜 로그인");
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }
    }

    private Member createNewMember(String username, String email, String memberName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        Member member = Member.builder()
                .username(username)
                .memberName(memberName)
                .email(email)
                .password(password)
                .role(MemberEnum.CUSTOMER)
                .build();
        return memberRepository.save(member);
    }
}
