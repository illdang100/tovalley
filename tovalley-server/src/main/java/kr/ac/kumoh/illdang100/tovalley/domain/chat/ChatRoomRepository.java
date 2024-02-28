package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    @Query("SELECT c FROM ChatRoom c WHERE " +
            "(c.sender.id = :memberId AND c.recipient.nickname = :otherMemberNickname) OR " +
            "(c.recipient.id = :memberId AND c.sender.nickname = :otherMemberNickname)")
    Optional<ChatRoom> findByMemberIdAndOtherMemberNickname(@Param("memberId") Long memberId, @Param("otherMemberNickname") String otherMemberNickname);

    @Query("select cr from ChatRoom cr join cr.sender s join cr.recipient r where cr.id = :chatRoomId and (s.id = :memberId or r.id = :memberId)")
    Optional<ChatRoom> findByIdAndMemberId(@Param("chatRoomId")Long chatRoomId, @Param("memberId")Long memberId);

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.sender s JOIN FETCH cr.recipient r WHERE cr.id = :id")
    Optional<ChatRoom> findByIdWithMembers(@Param("id") Long id);
}
