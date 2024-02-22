package kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka;

import java.io.Serializable;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Kafka를 통해 전송되는 메시지의 데이터 구조
/*
메시지는 Producer에서 생성되어 Kafka Broker로 전송되고, 이후 Consumer가 해당 메시지를 받아 처리하게 된다.
이때 메시지의 데이터 형태를 일관성 있게 유지하기 위해 Message 클래스와 같은 도메인 모델을 사용한다.
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

//    private String id;

    private Long chatRoomId;

    private Long senderId;

    private String content;

    private String createdAt;
    private int readCount;

//    private String contentType;

    public void prepareMessageForSending(Long senderId, String createdAt, int readCount) {
        this.senderId = senderId;
        this.createdAt = createdAt;
        this.readCount = readCount;
    }

    public ChatMessage convertToChatMessage() {
        return ChatMessage.builder()
//                .id(id)
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .createdAt(createdAt)
                .readCount(readCount)
                .build();
    }
}

