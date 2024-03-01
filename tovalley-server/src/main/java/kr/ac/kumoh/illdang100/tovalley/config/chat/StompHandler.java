package kr.ac.kumoh.illdang100.tovalley.config.chat;

import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatService;
import kr.ac.kumoh.illdang100.tovalley.service.chat.KafkaSender;
import kr.ac.kumoh.illdang100.tovalley.service.notification.NotificationService;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import kr.ac.kumoh.illdang100.tovalley.util.KafkaVO;
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
    private final NotificationService notificationService;
    private final ChatRoomRepository chatRoomRepository;
    private final KafkaSender kafkaSender;

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
        switch (stompCommand) {
            case CONNECT:
                log.debug("CONNECT");
                break;
            case SUBSCRIBE:
                handleSubscribe(accessor);
                break;
            case UNSUBSCRIBE:
                handleUnsubscribe(accessor);
                break;
            case SEND:
                log.debug("SEND");
                break;
            case DISCONNECT:
                handleDisconnect(accessor);
                break;
            case ERROR:
                log.debug("WebSocket Error 처리 코드!!");
                break;
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        if (accessor.getDestination().startsWith(TOPIC_NOTIFICATION)) {
            log.debug("알림 SUBSCRIBE");
            return;
        }

        handleChatRoomSubscription(accessor);
    }

    private void handleChatRoomSubscription(StompHeaderAccessor accessor) {
        log.debug("채팅방 SUBSCRIBE");

        Long chatRoomId = getChatRoomId(accessor);
        Long memberId = getMemberId(accessor);

        // 구독하려는 채팅방 참여자가 맞는지 검증
        validateChatRoomParticipant(chatRoomId, memberId);

        updateSubscription(accessor, chatRoomId, memberId);

        chatService.getOtherMemberIdByChatRoomId(chatRoomId, memberId)
                .ifPresent(otherMemberId -> notifyReadCountUpdate(chatRoomId, otherMemberId));

        notificationService.deleteAllNotificationsInChatRoomByMember(memberId, chatRoomId);
    }

    private void updateSubscription(StompHeaderAccessor accessor, Long chatRoomId, Long memberId) {
        // 이미 구독중인 방이 존재한다면 삭제
        deleteExistingSubscription(accessor);
        chatService.deleteChatRoomParticipantFromRedis(memberId);
        // 현재 채팅방 구독 정보를 웹소켓 세션에 저장
        accessor.getSessionAttributes().put(ChatUtil.SUBSCRIPTIONS, chatRoomId);
        // 채팅방 입장을 Redis에 저장
        chatService.saveChatRoomParticipantToRedis(memberId, chatRoomId);
        // 읽지 않은 메시지 읽음 처리
        chatService.updateUnreadMessages(memberId, chatRoomId);
    }

    private void deleteExistingSubscription(StompHeaderAccessor accessor) {
        Long subscriptions = (Long) accessor.getSessionAttributes().get(ChatUtil.SUBSCRIPTIONS);
        if (subscriptions != null) {
            accessor.getSessionAttributes().remove(ChatUtil.SUBSCRIPTIONS);
        }
    }

    private void notifyReadCountUpdate(Long chatRoomId, Long otherMemberId) {
        log.debug("상대방에게 readCount값 갱신 알림 전송");
        Notification readCountUpdateNotification = Notification.createReadCountUpdateNotification(chatRoomId, otherMemberId);
        kafkaSender.sendNotification(KafkaVO.KAFKA_NOTIFICATION_TOPIC, readCountUpdateNotification);
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        if (accessor.getDestination().startsWith(TOPIC_NOTIFICATION)) {
            log.debug("알림 UNSUBSCRIBE");
            return;
        }

        handleChatRoomUnsubscription(accessor);
    }

    private void handleChatRoomUnsubscription(StompHeaderAccessor accessor) {
        log.debug("채팅방 UNSUBSCRIBE");

        Long memberId = getMemberId(accessor);
        deleteExistingSubscription(accessor);
        chatService.deleteChatRoomParticipantFromRedis(memberId);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        log.debug("웹소켓 DISCONNECT");
        handleChatRoomUnsubscription(accessor);
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
}
