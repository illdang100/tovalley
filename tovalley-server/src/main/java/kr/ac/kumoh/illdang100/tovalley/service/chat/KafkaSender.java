package kr.ac.kumoh.illdang100.tovalley.service.chat;

import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, Message> kafkaChatTemplate;
    private final KafkaTemplate<String, Notification> kafkaNotificationTemplate;

    // 메시지를 지정한 Kafka 토픽으로 전송
    public void sendMessage(String topic, String chatRoomId, Message message) {

        // KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
        kafkaChatTemplate.send(topic, chatRoomId, message);
    }

    // (알림 토픽 + 상대방 pk)로 알림 메시지 전송하기!!
    public void sendNotification(String topic, String chatRoomId, Notification notification) {

        // KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
        kafkaNotificationTemplate.send(topic, chatRoomId, notification);
    }
}
