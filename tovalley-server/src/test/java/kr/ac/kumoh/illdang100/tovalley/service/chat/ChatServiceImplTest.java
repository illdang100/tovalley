package kr.ac.kumoh.illdang100.tovalley.service.chat;

import static kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessage;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest extends DummyObject {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("createOrGetChatRoom- 성공(생성x) 테스트")
    public void createOrGetChatRoom_return_test() {

        // given
        Long senderId = 1L;
        String recipientNick = "recipientNick";
        CreateNewChatRoomReqDto reqDto = new CreateNewChatRoomReqDto(recipientNick);

        // stub1
        Member sender = newMockMember(senderId, "username1", "nick1", CUSTOMER);
        Member recipient = newMockMember(2L, "username2", recipientNick, CUSTOMER);
        ChatRoom chatRoom = newMockChatRoom(10L, sender, recipient);
        when(chatRoomRepository.findBySenderIdAndRecipientNickname(senderId, reqDto.getRecipientNick()))
                .thenReturn(Optional.ofNullable(chatRoom));

        // when
        CreateNewChatRoomRespDto result = chatService.createOrGetChatRoom(senderId, reqDto);

        // then
        Assertions.assertThat(result.getChatRoomId()).isEqualTo(10L);
        Assertions.assertThat(result.isNew()).isFalse();
    }

    @Test
    @DisplayName("createOrGetChatRoom- 성공(생성o) 테스트")
    public void createOrGetChatRoom_create_test() {

        // given
        Long senderId = 1L;
        CreateNewChatRoomReqDto reqDto = new CreateNewChatRoomReqDto("recipientNick");

        // stub1
        String recipientNick = reqDto.getRecipientNick();
        when(chatRoomRepository.findBySenderIdAndRecipientNickname(senderId, recipientNick))
                .thenReturn(Optional.empty());

        // stub2
        Member sender = newMockMember(senderId, "username1", "nick1", CUSTOMER);
        Member recipient = newMockMember(2L, "username2", recipientNick, CUSTOMER);
        when(memberRepository.findByIdOrNickname(senderId, reqDto.getRecipientNick()))
                .thenReturn(new ArrayList<>(
                        Arrays.asList(sender, recipient)));

        // stub3
        Long chatRoomId = 10L;
        ChatRoom createdChatRoom = newMockChatRoom(chatRoomId, sender, recipient);
        when(chatRoomRepository.save(any())).thenReturn(createdChatRoom);

        // when
        CreateNewChatRoomRespDto result = chatService.createOrGetChatRoom(senderId, reqDto);

        // then
        Assertions.assertThat(result.getChatRoomId()).isEqualTo(10L);
        Assertions.assertThat(result.isNew()).isTrue();
    }

    @Test
    @DisplayName("createOrGetChatRoom- 사용자 없는 예외 테스트")
    public void createOrGetChatRoom_fail_test() {

        // given
        Long senderId = 1L;
        CreateNewChatRoomReqDto reqDto = new CreateNewChatRoomReqDto("recipientNick");

        // stub1
        String recipientNick = reqDto.getRecipientNick();
        when(chatRoomRepository.findBySenderIdAndRecipientNickname(senderId, recipientNick))
                .thenReturn(Optional.empty());

        // stub2
        Member sender = newMockMember(senderId, "username1", "nick1", CUSTOMER);
        when(memberRepository.findByIdOrNickname(senderId, reqDto.getRecipientNick()))
                .thenReturn(new ArrayList<>(Arrays.asList(sender)));

        // when

        // then
        Assertions.assertThatThrownBy(() -> chatService.createOrGetChatRoom(senderId, reqDto))
                .isInstanceOf(CustomApiException.class);
    }

    @Test
    public void getChatRoomSlice_success_test() {

        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        ChatRoomRespDto mockChatRoomRespDto = ChatRoomRespDto.builder()
                .chatRoomId(1L)
                .chatRoomTitle("Test title")
                .otherUserProfileImage("afsoijdosfiajasdofij")
                .otherUserNick("testNick")
                .createdChatRoomDate(LocalDateTime.now())
                .build();

        List<ChatRoomRespDto> chatRoomRespDtoList = new ArrayList<>();
        chatRoomRespDtoList.add(mockChatRoomRespDto);
        Slice<ChatRoomRespDto> mockSlice = new SliceImpl<>(chatRoomRespDtoList);

        ChatMessage mockChatMessage = ChatMessage.builder()
                .chatRoomId(1L)
                .senderId(2L)
                .content("Test message")
                .createdAt(ZonedDateTime.now().toString())
                .build();

        // stub1
        when(chatRoomRepository.findSliceChatRoomsByMemberId(memberId, pageable)).thenReturn(mockSlice);

        // stub2
        when(chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(mockChatRoomRespDto.getChatRoomId()))
                .thenReturn(Optional.ofNullable(mockChatMessage));

        // when
        Slice<ChatRoomRespDto> result = chatService.getChatRoomSlice(memberId, pageable);

        // then
        Assertions.assertThat(result.getContent().get(0).getChatRoomId())
                .isEqualTo(mockChatRoomRespDto.getChatRoomId());
        Assertions.assertThat(result.getContent().get(0).getLastMessageContent())
                .isEqualTo(mockChatMessage.getContent());
        Assertions.assertThat(result.getContent().get(0).getLastMessageTime())
                .isEqualTo(ChatUtil.convertZdtStringToLocalDateTime(mockChatMessage.getCreatedAt()));
    }

    @Test
    public void getChatMessages_test() {

        // given
        Long memberId = 1L;
        Long chatRoomId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "createdAt");

        Member sender = newMockMember(memberId, "username1", "nickname1", CUSTOMER);
        Member recipient = newMockMember(memberId, "username2", "nickname2", CUSTOMER);
        ChatRoom chatRoom = newMockChatRoom(chatRoomId, sender, recipient);

        // 채팅 메시지 목록 생성
        List<ChatMessage> mockChatMessages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockChatMessages.add(ChatMessage.builder()
                    .id("chatMessageId" + i)
                    .senderId(sender.getId())
                    .content("Test Message " + i)
                    .createdAt(ZonedDateTime.now().minusMinutes(i).toString())
                    .build());
        }

        // stub1
        when(chatRoomRepository.findByIdAndMemberId(chatRoomId, memberId)).thenReturn(Optional.of(chatRoom));

        // stub2
        when(chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable))
                .thenReturn(new SliceImpl<>(mockChatMessages, pageable, true));

        // when
        Slice<ChatMessageListRespDto> result = chatService.getChatMessages(memberId, chatRoomId, pageable);

        // then
        Assertions.assertThat(result.getContent().get(0).getChatMessages().size()).isEqualTo(10);
        Assertions.assertThat(result.getContent().get(0).getChatMessages().get(0).getSenderId()).isEqualTo(sender.getId());
        Assertions.assertThat(result.getContent().get(0).getChatMessages().get(0).getContent()).isEqualTo("Test Message 0");
        Assertions.assertThat(result.getContent().get(0).getChatMessages().get(9).getContent()).isEqualTo("Test Message 9");
        Assertions.assertThat(result.getContent().get(0).getChatRoomId()).isEqualTo(chatRoomId);
    }
}