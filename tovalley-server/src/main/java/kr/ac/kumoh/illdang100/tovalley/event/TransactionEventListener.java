package kr.ac.kumoh.illdang100.tovalley.event;

import kr.ac.kumoh.illdang100.tovalley.service.chat.KafkaSender;
import kr.ac.kumoh.illdang100.tovalley.util.KafkaVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private final KafkaSender kafkaSender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSendMessageSuccess(SendMessageEvent event) {
        kafkaSender.sendMessage(KafkaVO.KAFKA_CHAT_TOPIC, String.valueOf(event.getChatRoomId()), event.getMessage());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSendNotificationSuccess(SendNotificationEvent event) {
        kafkaSender.sendNotification(KafkaVO.KAFKA_NOTIFICATION_TOPIC,
                String.valueOf(event.getChatRoomId()),
                event.getNotification());
    }
}
