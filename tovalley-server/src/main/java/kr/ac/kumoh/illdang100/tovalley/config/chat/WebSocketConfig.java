package kr.ac.kumoh.illdang100.tovalley.config.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // WebSocket을 활성화하고 메시지 브로커 사용가능
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private final HttpHandshakeInterceptor httpHandshakeInterceptor;

    // TODO: 서버 측에서 클라이언트의 연결 상태를 주기적으로 확인하고, 오랫동안 응답이 없는 클라이언트에 대한 연결 종료하는 방식 알아보기!!
    // 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성
    // STOMP를 사용하는 경우 Kafka와 같은 브로커 사용이 가능하다.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.debug("WebSocketConfig.configureMessageBroker 동작!!");
        // Simple 메시지 브로커가 사용할 prefix를 설정
        // 클라이언트가 메시지를 구독할 endpoint를 "/queue", "/topic"로 지정
        /*
        인자로 받은 경로(prefix)로 시작하는 메시지를 구독자에게 전달하는 역할을 한다.
        즉, 클라이언트가 특정 주제를 구독하고 있을 때, 그 주제에 대한 메시지가 발행되면 해당 메시지를 받을 수 있게 해준다.
        예를 들어, "/topic", "/queue" 등의 prefix를 사용할 수 있다.

        클라이언트는 이 prefix를 사용하여 메시지를 구독하거나 발행할 수 있다.
        예를 들어, 클라이언트가 "/queue/{chatNo}" 주제를 구독하면, 이 주제에 메시지가 발행되면 해당 메시지를 받을 수 있다.
         */
        // 클라이언트에서 1번 채널을 구독하고자 할 때는 /sub/1 형식과 같은 규칙을 따라야 한다.
        registry.enableSimpleBroker("/sub"); // /sub/{chatNo}로 주제 구독 가능

        // 메시지 핸들러로 라우팅 되는 Prefix
        // 메시지를 보낼 endpoint를 "/pub"로 지정
        // /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다.
        registry.setApplicationDestinationPrefixes("/pub"); //pub/message로 메시지 전송 컨트롤러 라우팅 가능
    }

    // 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.debug("WebSocketConfig.registerStompEndpoints 동작!!");
        /*
        사용자가 채팅방에 입장하면, 웹 클라이언트는 ws://your-server/chat URL을 통해 웹소켓 서버에 연결을 시도한다.
        이때, 서버의 웹소켓 설정에서 addEndpoint("/chat")를 통해 /chat endpoint가 웹소켓 연결을 받아들일 수 있도록 설정되어 있기 때문에,
        클라이언트의 연결 요청은 성공하게 된다.
        이후 클라이언트와 서버 간에는 웹소켓을 통해 실시간으로 데이터를 주고받을 수 있게 된다.
         */
        registry.addEndpoint("/stomp/chat") // STOMP 엔드포인트 설정 -> Spring Security 설정에서 접근 가능하도록 등록해야함.
                .setAllowedOriginPatterns("*"); // 모든 Origin 허용 -> 배포시에는 보안을 위해 Origin을 정확히 지정
//                .addInterceptors(httpHandshakeInterceptor);
//                .withSockJS(); // SockJS 사용가능 설정(SockJS는 웹소켓을 지원하지 않는 브라우저에서도 백업 옵션을 통해 웹소켓과 유사한 객체를 제공)
    }

    // 클라이언트 인바운드 채널을 구성하는 메서드
    // 사용자가 웹 소켓 연결에 연결 될 때와 끊길 때 추가 기능(인증, 세션 관리 등)을 위한 인터셉터 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.debug("WebSocketConfig.configureClientInboundChannel 동작!!");
        // stompHandler를 인터셉터로 등록하여 STOMP 메시지 핸들링을 수행
        registration.interceptors(stompHandler);
    }

    // STOMP에서 64KB 이상의 데이터 전송을 못하는 문제 해결
    /*@Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(160 * 64 * 1024);
        registry.setSendTimeLimit(100 * 10000);
        registry.setSendBufferSizeLimit(3 * 512 * 1024);
    }*/
}
