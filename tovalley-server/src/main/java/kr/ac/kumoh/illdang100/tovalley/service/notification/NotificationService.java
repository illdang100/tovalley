package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.util.List;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationService {

    void sendNotification(Message message, Long senderId, Long chatRoomId);

    Slice<ChatNotificationRespDto> getChatNotificationsByMemberId(Long memberId, Pageable pageable);

    void deleteChatNotifications(List<String> chatNotifications);
}
