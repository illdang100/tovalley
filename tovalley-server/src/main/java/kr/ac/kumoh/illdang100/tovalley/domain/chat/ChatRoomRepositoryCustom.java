package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomRepositoryCustom {
    Slice<ChatRoomRespDto> findSliceChatRoomsByMemberId(Long memberId, Pageable pageable);
}
