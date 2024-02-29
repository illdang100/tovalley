package kr.ac.kumoh.illdang100.tovalley.domain.notification;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {

    @EntityGraph(attributePaths = {"sender"})
    List<ChatNotification> findWithSenderByRecipientIdOrderByCreatedDateDesc(Long recipientId);
}
