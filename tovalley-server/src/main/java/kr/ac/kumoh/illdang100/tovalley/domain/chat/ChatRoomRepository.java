package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    Optional<ChatRoom> findBySenderIdAndRecipientNickname(Long senderId, String recipientNick);

    @Query("select cr from ChatRoom cr join cr.sender s join cr.recipient r where cr.id = :chatRoomId and s.id = :memberId or r.id = :memberId")
    Optional<ChatRoom> findByIdAndMemberId(@Param("chatRoomId")Long chatRoomId, @Param("memberId")Long memberId);
}
