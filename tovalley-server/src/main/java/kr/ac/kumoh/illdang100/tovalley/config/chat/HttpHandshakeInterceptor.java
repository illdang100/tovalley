package kr.ac.kumoh.illdang100.tovalley.config.chat;

import static kr.ac.kumoh.illdang100.tovalley.util.CookieUtil.findCookieValue;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
// Spring Security와 Spring WebSocket을 함께 쓴다면 HandshakeInterceptor를 구현해 유저의 인증을 처리할 수 있다.
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProcess jwtProcess;

    private static final String ERROR_MESSAGE = "웹소켓 연결 오류!!";

    /*
    일반적으로 웹소켓에서의 사용자 인증은 핸드셰이크 과정에서 한 번만 수행하고, 그 이후에는 웹소켓 연결이 유지되는 동안 해당 연결을 신뢰하는 방식을 사용한다.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

        String accessToken = extractAccessToken(req);
        return verifyTokenAndStoreMemberId(accessToken, attributes, resp);
    }

    private String extractAccessToken(HttpServletRequest req) {
        return findCookieValue(req, JwtVO.ACCESS_TOKEN).replace(JwtVO.TOKEN_PREFIX, "");
    }

    private boolean verifyTokenAndStoreMemberId(String accessToken, Map<String, Object> attributes, HttpServletResponse resp)
            throws IOException {
        try {
            PrincipalDetails principalDetails = jwtProcess.verify(accessToken);
            Long memberId = principalDetails.getMember().getId();

            log.debug("beforeHandshake - memberId={}", memberId);
            attributes.put(ChatUtil.MEMBER_ID, memberId);

            return true;
        } catch (Exception e) {
            log.error(ERROR_MESSAGE);
            CustomResponseUtil.handleTokenVerificationFailure(resp);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
