package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kr.ac.kumoh.illdang100.tovalley.domain.notification.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.domain.notification.ChatNotificationRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.NotificationType;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.service.chat.KafkaSender;
import kr.ac.kumoh.illdang100.tovalley.util.KafkaVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
                    sender.getImageFile() != null ? sender.getImageFile().getStoreFileUrl() : null,
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
    public Slice<ChatNotificationRespDto> getChatNotificationsByMemberId(Long memberId, Long cursorId, Pageable pageable) {
        Slice<ChatNotification> sliceChatNotifications
                = chatNotificationRepository.findSliceChatNotificationsByMemberId(memberId, cursorId, pageable);

        List<ChatNotification> notifications = sliceChatNotifications.getContent();

        List<ChatNotificationRespDto> notificationDtos = mapToDto(notifications);

        markNotificationsAsRead(notifications);

        return new SliceImpl<>(notificationDtos, pageable, sliceChatNotifications.hasNext());
    }

    private List<ChatNotificationRespDto> mapToDto(List<ChatNotification> chatNotifications) {
        return chatNotifications.stream()
                .map(ChatNotificationRespDto::new)
                .collect(Collectors.toList());
    }

    private void markNotificationsAsRead(List<ChatNotification> chatNotifications) {
        for (ChatNotification chatNotification : chatNotifications) {
            if (!chatNotification.getHasRead()) {
                chatNotification.changeHasReadToTrue();
            }
        }
    }

    @Override
    @Transactional
    public void deleteSingleChatNotification(Long memberId, Long chatNotificationId) {
        int deletedCount = chatNotificationRepository.deleteByIdAndRecipientId(chatNotificationId, memberId);

        if (deletedCount == 0) {
            throw new CustomApiException("해당 알림을 삭제할 권한이 없습니다");
        }
    }

    @Override
    @Transactional
    public void deleteAllNotificationsOfMember(Long memberId) {
        chatNotificationRepository.deleteByRecipientId(memberId);
    }

    @Override
    @Transactional
    public void deleteAllNotificationsInChatRoomByMember(Long memberId, Long chatRoomId) {
        chatNotificationRepository.deleteByRecipientIdAndChatRoomId(memberId, chatRoomId);
    }
}
