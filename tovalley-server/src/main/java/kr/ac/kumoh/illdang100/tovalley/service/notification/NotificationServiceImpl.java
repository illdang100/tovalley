package kr.ac.kumoh.illdang100.tovalley.service.notification;

import java.util.List;
import java.util.stream.Collectors;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotificationRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;
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
