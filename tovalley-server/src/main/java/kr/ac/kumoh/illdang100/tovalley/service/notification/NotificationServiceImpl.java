package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotificationRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.NotificationType;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;
import kr.ac.kumoh.illdang100.tovalley.service.chat.KafkaSender;
import kr.ac.kumoh.illdang100.tovalley.util.KafkaVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final ChatNotificationRepository chatNotificationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final KafkaSender kafkaSender;

    @Override
    public void sendNotification(Message message, Long senderId, Long chatRoomId) {
        try {
            ChatRoom findChatRoom = chatRoomRepository.findByIdWithMembers(chatRoomId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

            Member recipient = getRecipientFromChatRoom(findChatRoom, senderId);
            Member sender = getSenderFromChatRoom(findChatRoom, senderId);

            // 알림 정보를 RDBMS에 저장
            chatNotificationRepository.save(
                    new ChatNotification(sender, recipient.getId(), chatRoomId, message.getContent()));

            // Kafka로는 Notification 객체 전송
            Notification notification = new Notification(chatRoomId, recipient.getId(), sender.getNickname(),
                    LocalDateTime.now(), message.getContent(), NotificationType.CHAT);

            // "알림 토픽 + {memberId}"로 알림 메시지 전송하기!!
            kafkaSender.sendNotification(KafkaVO.KAFKA_NOTIFICATION_TOPIC, notification);
        } catch (Exception e) {
            log.error("메시지 알림 전송 에러");
        }
    }

    private Member getRecipientFromChatRoom(ChatRoom findChatRoom, Long senderId) {
        return findChatRoom.getSender().getId().equals(senderId) ? findChatRoom.getRecipient()
                : findChatRoom.getSender();
    }

    private Member getSenderFromChatRoom(ChatRoom findChatRoom, Long senderId) {
        return findChatRoom.getSender().getId().equals(senderId) ? findChatRoom.getSender()
                : findChatRoom.getRecipient();
    }

    @Override
    @Transactional
    public List<ChatNotificationRespDto> getChatNotificationsByMemberId(Long memberId) {
        List<ChatNotification> chatNotifications = chatNotificationRepository.findWithSenderByRecipientIdOrderByCreatedDateDesc(
                memberId);

        List<ChatNotificationRespDto> result = convertToResponseDto(chatNotifications);

        for (ChatNotification chatNotification : chatNotifications) {
            if (!chatNotification.getHasRead()) {
                chatNotification.changeHasReadToTrue();
            }
        }

        updateHasReadToTrue(chatNotifications);

        return result;
    }

    private List<ChatNotificationRespDto> convertToResponseDto(List<ChatNotification> chatNotifications) {
        return chatNotifications.stream()
                .map(ChatNotificationRespDto::new)
                .collect(Collectors.toList());
    }

    private void updateHasReadToTrue(List<ChatNotification> chatNotifications) {
        for (ChatNotification chatNotification : chatNotifications) {
            if (!chatNotification.getHasRead()) {
                chatNotification.changeHasReadToTrue();
            }
        }
    }

    @Override
    public void deleteChatNotifications(List<String> chatNotifications) {

    }
}
