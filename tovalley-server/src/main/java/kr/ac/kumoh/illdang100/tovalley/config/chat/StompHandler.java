package kr.ac.kumoh.illdang100.tovalley.config.chat;

import java.util.Map;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatService;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
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

    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    private static final String TOPIC_NOTIFICATION = "/sub/notification";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { // 메시지의 유효성 검사나 변형 처리
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand stompCommand = accessor.getCommand();

        if (stompCommand != null) {
            handleStompCommand(stompCommand, accessor);
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private void handleStompCommand(StompCommand stompCommand, StompHeaderAccessor accessor) {
        if (stompCommand.equals(StompCommand.CONNECT)) {
            log.debug("CONNECT");
        } else if (stompCommand.equals(StompCommand.SUBSCRIBE)) {
            handleSubscribe(accessor);
        } else if (stompCommand.equals(StompCommand.UNSUBSCRIBE)) {
            handleUnsubscribe(accessor);
        } else if (stompCommand.equals(StompCommand.SEND)) {
            log.debug("SEND");
        } else if (stompCommand.equals(StompCommand.DISCONNECT)) {
            handleDisconnect(accessor);
        } else if (stompCommand.equals(StompCommand.ERROR)) {
            log.debug("WebSocket Error 처리 코드!!");
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination.startsWith(TOPIC_NOTIFICATION)) {
            log.debug("알림 SUBSCRIBE");
            return;
        }

        Long chatRoomId = getChatRoomId(accessor);
        Long memberId = getMemberId(accessor);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long subscriptions = (Long) sessionAttributes.get(ChatUtil.SUBSCRIPTIONS);

        log.debug("채팅방 SUBSCRIBE");

        // 구독하려는 채팅방 참여자가 맞는지 검증
        validateChatRoomParticipant(chatRoomId, memberId);

        // 이미 구독중인 방이 존재한다면 삭제
        deleteSubscriptionIfNotNull(sessionAttributes, subscriptions);
        chatService.deleteChatRoomParticipantFromRedis(memberId);

        // 현재 채팅방 구독 정보를 웹소켓 세션에 저장
        sessionAttributes.put(ChatUtil.SUBSCRIPTIONS, chatRoomId);

        // 채팅방 입장을 Redis에 저장
        chatService.saveChatRoomParticipantToRedis(memberId, chatRoomId);

        // 읽지 않은 메시지 읽음 처리
        chatService.updateUnreadMessages(memberId, chatRoomId);
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination.startsWith(TOPIC_NOTIFICATION)) {
            log.debug("알림 UNSUBSCRIBE");
            return;
        }

        log.debug("채팅방 UNSUBSCRIBE");
        Long memberId = getMemberId(accessor);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long subscriptions = (Long) sessionAttributes.get(ChatUtil.SUBSCRIPTIONS);

        deleteSubscriptionIfNotNull(sessionAttributes, subscriptions);
        chatService.deleteChatRoomParticipantFromRedis(memberId);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        Long memberId = getMemberId(accessor);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long subscriptions = (Long) sessionAttributes.get(ChatUtil.SUBSCRIPTIONS);

        deleteSubscriptionIfNotNull(sessionAttributes, subscriptions);
        chatService.deleteChatRoomParticipantFromRedis(memberId);
    }

    private void validateChatRoomParticipant(Long chatRoomId, Long memberId) {
        // RDBMS로부터 데이터 조회가 성능 저하를 일으킬 수 있지만, 채팅방 구독 시에만 발생하니 괜찮다.
        chatRoomRepository.findByIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방에 참여하지 않은 사용자는 구독할 수 없습니다"));
    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        return Long.valueOf(accessor.getDestination().split("/")[4]);
    }

    private Long getMemberId(StompHeaderAccessor accessor) {
        return (Long) accessor.getSessionAttributes().get(ChatUtil.MEMBER_ID);
    }

    private void deleteSubscriptionIfNotNull(Map<String, Object> sessionAttributes, Long subscriptions) {
        if (subscriptions != null) {
            sessionAttributes.remove(ChatUtil.SUBSCRIPTIONS);
        }
    }
}
