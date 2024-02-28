package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.util.List;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;

public interface NotificationService {

    void sendNotification(Message message, Long senderId, Long chatRoomId);

    List<ChatNotificationRespDto> getChatNotificationsByMemberId(Long memberId);

    void deleteChatNotifications(List<String> chatNotifications);
}
