package kr.ac.kumoh.illdang100.tovalley.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long>, ChatNotificationRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ChatNotification cn WHERE cn.id =:id AND cn.recipientId =:recipientId")
    int deleteByIdAndRecipientId(@Param("id") Long id, @Param("recipientId") Long recipientId);

    void deleteByRecipientId(Long recipientId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ChatNotification cn WHERE cn.recipientId =:recipientId AND cn.chatRoomId =:chatRoomId")
    void deleteByRecipientIdAndChatRoomId(@Param("recipientId") Long recipientId, @Param("chatRoomId") Long chatRoomId);
}
