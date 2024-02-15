package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    Optional<ChatRoom> findBySenderIdAndRecipientNickname(Long senderId, String recipientNick);
}
