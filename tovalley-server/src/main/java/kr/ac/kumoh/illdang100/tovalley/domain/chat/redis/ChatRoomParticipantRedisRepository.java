package kr.ac.kumoh.illdang100.tovalley.domain.chat.redis;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomParticipantRedisRepository extends CrudRepository<ChatRoomParticipant, String> {

    List<ChatRoomParticipant> findByMemberId(Long memberId);

    List<ChatRoomParticipant> findByChatRoomId(Long chatRoomId);
}
