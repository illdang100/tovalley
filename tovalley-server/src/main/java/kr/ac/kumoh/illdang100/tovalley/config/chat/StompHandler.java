package kr.ac.kumoh.illdang100.tovalley.config.chat;

import java.util.Objects;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatRoomService;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor { // WebSocket을 이용한 채팅 기능에서 메시지를 가공하고 처리하는 역할

    private final JwtProcess jwtProcess;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    // 웹소켓 연결 시 생성된 세션의 Access Token으로부터 사용자 유효성 검사 수행
    // TODO: 하나의 채팅방에 최대 2명의 사용자만 입장 가능 기능 구현
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { // 메시지의 유효성 검사나 변형 처리

        log.debug("StompHandler.preSend 동작!!");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        handleMessage(accessor.getCommand(), accessor);

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {
        switch (stompCommand) {

            case CONNECT: // HandshakeInterceptor 이후 웹소켓 연결이 성공적으로 이루어졌을 때 실행된다.
                // TODO: 실제 테스트 해보니 웹소켓 연결하고 실행이 안됨!!
                log.debug("CONNECT");
                // 서버에서 클라이언트로 응답을 보낼 때 사용되는 명령어

//                connectToChatRoom(accessor);

                /*
                CONNECT
                1. 사용자 인증: CONNECT 명령을 통해 웹소켓 연결이 처음 이루어질 때, 일반적으로 사용자의 인증 정보를 서버에 전송한다.
                서버는 이 정보를 통해 사용자를 인증하고, 인증된 사용자에게만 웹소켓 연결을 허용합니다. 사용자 인증은 보안을 위해 필수적인 절차이다.
                2. 세션 생성: 웹소켓 연결이 성공적으로 이루어지면, 서버는 이 연결에 대한 세션을 생성한다.
                세션은 연결된 클라이언트를 식별하고 상태 정보를 유지하는 데 사용된다.
                3. 연결 상태 관리: 서버는 연결이 성공적으로 이루어졌음을 클라이언트에게 알리고, 연결 상태를 관리한다.
                예를 들어, 연결이 끊어진 클라이언트를 감지하거나, 연결이 유지되는 동안 필요한 하트비트 메시지를 전송하는 등의 작업을 수행한다.
                 */

                break;
            case STOMP:
                log.debug("STOMP");
                break;
            case SUBSCRIBE: // 채팅방 구독 시 호출됨
                log.debug("SUBSCRIBE");
                // TODO: 해당 채팅방의 사용자인지 검증하는 코드 필요!!
                // TODO: 해당 채팅방이 실제로 존재하는 채팅방인지 체크하는 코드 필요!!
                // TODO: 이미 구독 중인 채팅방이 있는 경우(중복 구독 요청) 처리

                /*
                SUBSCRIBE
                1. 주제 구독: 클라이언트는 SUBSCRIBE 명령을 통해 서버의 특정 주제(Topic)를 구독합니다.
                이 주제는 일반적으로 채팅방을 나타내며, 클라이언트는 이 주제에 대한 메시지를 받을 수 있게 됩니다.
                2. 권한 검사: 서버는 SUBSCRIBE 명령을 받을 때, 해당 클라이언트가 주제를 구독할 권한이 있는지 검사합니다.
                예를 들어, 특정 채팅방에 대한 접근 권한을 검사하여, 채팅방의 멤버만 해당 채팅방의 메시지를 받을 수 있게 할 수 있습니다.
                3. 메시지 전송: 클라이언트가 주제를 구독하면, 서버는 이후에 해당 주제에 대한 메시지가 발행될 때마다 이를 클라이언트에게 전송합니다.
                클라이언트는 이 메시지를 받아 채팅 메시지를 처리하게 됩니다.
                 */
                break;
            case SEND:
                log.debug("SEND");
                break;
            case DISCONNECT:
                log.debug("DISCONNECT");
                // TODO: 채팅방(ChatRoom)의 사용자 수 필드 감소
                break;
            case ERROR:
                log.debug("WebSocket Error 처리 코드!!");
                break;
        }
    }

    private void connectToChatRoom(StompHeaderAccessor accessor) {
        // TODO: 그 사람과의 채팅방이 없다면 새로 만들고 있다면 해당 채팅방 들어가기?? -> 이거는 preSend라서 연결되고 메시지 보내기 전인데?
        // 채팅방 번호를 가져온다.
        Long chatRoomId = getChatRoomId(accessor);
    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        return
                Long.parseLong(
                        Objects.requireNonNull(
                                accessor.getFirstNativeHeader("chatRoomId")
                        ));
    }
}
