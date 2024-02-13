package kr.ac.kumoh.illdang100.tovalley.service.chat;

import java.util.List;
import java.util.Optional;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessage;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 상대방과의 채팅방이 존재하지 않는다면, 새로운 채팅방 생성해서 응답 만약 상대방과 기존 채팅방이 존재한다면 기존 채팅방 pk 응답
     *
     * @param senderId   채팅방 생성을 요청하는 사용자 pk
     * @param requestDto
     * @return
     */
    @Override
    public CreateNewChatRoomRespDto createOrGetChatRoom(Long senderId, CreateNewChatRoomReqDto requestDto) {

        // 요청하는 사용자와 상대방 간에 채팅방이 있는지 확인하는 로직 필요
        String recipientNick = requestDto.getRecipientNick();

        Optional<Long> chatRoomIdOpt = chatRoomRepository.findIdBySenderIdAndRecipientNickname(
                senderId, recipientNick);


        // 존재한다면 기존 채팅방 Pk 반환
        if (chatRoomIdOpt.isPresent()) {
            return new CreateNewChatRoomRespDto(false, chatRoomIdOpt.get());
        }

        List<Member> members = memberRepository.findByIdOrNickname(senderId, recipientNick);
        validateMembers(members);

        Member sender = getSender(members, senderId);
        Member recipient = getRecipient(members, senderId);

        return createNewChatRoom(sender, recipient);
    }

    private void validateMembers(List<Member> members) {
        if (members.size() != 2) {
            throw new CustomApiException("사용자가 존재하지 않습니다");
        }
    }

    private Member getSender(List<Member> members, Long senderId) {
        for (Member member : members) {
            if (member.getId().equals(senderId)) {
                return member;
            }
        }
        throw new CustomApiException("발신자를 찾을 수 없습니다");
    }

    private Member getRecipient(List<Member> members, Long senderId) {
        for (Member member : members) {
            if (!member.getId().equals(senderId)) {
                return member;
            }
        }
        throw new CustomApiException("수신자를 찾을 수 없습니다");
    }

    private CreateNewChatRoomRespDto createNewChatRoom(Member sender, Member recipient) {
        ChatRoom chatRoom = ChatRoom.createNewChatRoom(sender, recipient);
        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
        return new CreateNewChatRoomRespDto(true, createdChatRoom.getId());
    }

    /**
     * 채팅방 목록을 Slice 형태로 전달
     *
     * @param memberId 채팅방 목록을 요청하는 사용자 pk
     * @param pageable 페이징 상세 정보
     * @return 채팅방 목록
     */
    @Override
    public Slice<ChatRoomRespDto> getChatRoomSlice(Long memberId, Pageable pageable) {
        Slice<ChatRoomRespDto> sliceChatRoomsByMemberId = chatRoomRepository.findSliceChatRoomsByMemberId(memberId,
                pageable);

        List<ChatRoomRespDto> content = sliceChatRoomsByMemberId.getContent();
        for (ChatRoomRespDto chatRoomRespDto : content) {

            Long chatRoomId = chatRoomRespDto.getChatRoomId();
            Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(
                    chatRoomId);

            lastMessageOpt.ifPresent((lastMessage) -> {
                chatRoomRespDto.changeLastMessage(lastMessage.getContent(),
                        ChatUtil.convertZdtStringToLocalDateTime(lastMessage.getCreatedAt()));
            });
        }

        return new SliceImpl<>(content, pageable, sliceChatRoomsByMemberId.hasNext());
    }

    @Override
    public ChatMessageListRespDto getChatMessages(Long memberId, Long chatRoomId) {
        return null;
    }
}
