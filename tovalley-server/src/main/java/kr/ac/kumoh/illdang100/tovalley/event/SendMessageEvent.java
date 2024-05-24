package kr.ac.kumoh.illdang100.tovalley.event;

import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendMessageEvent extends ApplicationEvent {
    private final Long chatRoomId;
    private final Message message;

    public SendMessageEvent(Object source, Long chatRoomId, Message message) {
        super(source);
        this.chatRoomId = chatRoomId;
        this.message = message;
    }
}
