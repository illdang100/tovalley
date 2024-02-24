package kr.ac.kumoh.illdang100.tovalley.web.api;

import static kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatService;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 API Document")
public class ChatApiController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @PostMapping("/api/auth/chatroom")
    public ResponseEntity<?> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid final CreateNewChatRoomReqDto requestDto,
            BindingResult bindingResult) {

        CreateNewChatRoomRespDto respDto =
                chatService.createOrGetChatRoom(principalDetails.getMember().getId(), requestDto);
        if (respDto.isNew()) {
            return new ResponseEntity<>(new ResponseDto<>(1, "채팅방이 생성되었습니다", respDto), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseDto<>(1, "기존 채팅방을 응답합니다", respDto), HttpStatus.OK);
        }
    }

    @GetMapping("/api/auth/chatroom")
    public ResponseEntity<?> findChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @PageableDefault(size = 10) Pageable pageable) {
        Slice<ChatRoomRespDto> result = chatService.getChatRooms(principalDetails.getMember().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방 목록 조회를 성공했습니다", result), HttpStatus.OK);
    }

    // TODO: 응답 dto 필드에서 myMsg 필드 제외 나머지를 Message 필드와 통일 시키기
    @GetMapping("/api/auth/chat/messages/{chatRoomId}")
    public ResponseEntity<?> findChatMessages(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(required = false) String lastMessageId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        ChatMessageListRespDto result
                = chatService.getChatMessages(principalDetails.getMember().getId(), chatRoomId, lastMessageId, pageable);

        if (result.getChatMessages().isEmpty()) {
            return new ResponseEntity<>(new ResponseDto<>(1, "더이상 채팅 메시지가 없습니다", result), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseDto<>(1, "채팅 메시지 목록 조회에 성공했습니다", result), HttpStatus.OK);
    }

    /*
    @MessageMapping은 Spring에서 제공하는 어노테이션으로,
    클라이언트에서 특정 주소로 메시지를 보냈을 때, 그 메시지를 받아 처리할 메서드에 붙이는 어노테이션
    이 어노테이션은 주로 WebSocket 통신에서 STOMP 메시지를 처리하는 데 사용된다.
     */
    @MessageMapping("/chat/message") //websocket "/pub/chat/message"로 들어오는 메시지 처리
    public void sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        Long memberId = (Long)headerAccessor.getSessionAttributes().get(ChatUtil.MEMBER_ID);
        chatService.sendMessage(message, memberId);
    }
}
