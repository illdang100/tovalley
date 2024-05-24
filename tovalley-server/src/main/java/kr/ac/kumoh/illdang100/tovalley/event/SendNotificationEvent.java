package kr.ac.kumoh.illdang100.tovalley.event;

import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendNotificationEvent extends ApplicationEvent {
    private final Long chatRoomId;
    private final Notification notification;

    public SendNotificationEvent(Object source, Long chatRoomId, Notification notification) {
        super(source);
        this.chatRoomId = chatRoomId;
        this.notification = notification;
    }
}
