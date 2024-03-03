package kr.ac.kumoh.illdang100.tovalley.service.chat;

import java.util.List;
import java.util.Optional;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import org.springframework.data.domain.Pageable;

public interface ChatService {

    CreateNewChatRoomRespDto createOrGetChatRoom(Long memberId, CreateNewChatRoomReqDto requestDto);

    List<ChatRoomRespDto> getChatRooms(Long memberId);

    ChatMessageListRespDto getChatMessages(Long memberId, Long chatRoomId, String cursor, Pageable pageable);

    void sendMessage(Message message, Long senderId);

    void saveChatRoomParticipantToRedis(Long memberId, Long chatRoomId);

    void deleteChatRoomParticipantFromRedis(Long memberId);

    void updateUnreadMessages(Long memberId, Long chatRoomId);

    Optional<Long> getOtherMemberIdByChatRoomId(Long chatRoomId, Long memberId);
}
