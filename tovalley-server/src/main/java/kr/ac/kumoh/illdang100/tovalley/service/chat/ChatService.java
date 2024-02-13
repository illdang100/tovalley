package kr.ac.kumoh.illdang100.tovalley.service.chat;

import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatService {

    CreateNewChatRoomRespDto createOrGetChatRoom(Long memberId, CreateNewChatRoomReqDto requestDto);

    Slice<ChatRoomRespDto> getChatRoomSlice(Long memberId, Pageable pageable);

    ChatMessageListRespDto getChatMessages(Long memberId, Long chatRoomId);


}
