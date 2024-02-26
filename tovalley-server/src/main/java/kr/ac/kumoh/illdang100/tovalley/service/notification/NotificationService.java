package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.util.List;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;

public interface NotificationService {

    List<ChatNotificationRespDto> getChatNotificationsByMemberId(Long memberId);

    void deleteChatNotifications(List<String> chatNotifications);
}
