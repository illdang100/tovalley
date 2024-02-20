package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {
}
