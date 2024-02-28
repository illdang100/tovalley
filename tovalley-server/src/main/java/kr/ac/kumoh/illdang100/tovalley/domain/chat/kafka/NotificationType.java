package kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    CHAT("채팅"), READ_COUNT_UPDATE("읽음 상태 변경");
    private final String value;
}