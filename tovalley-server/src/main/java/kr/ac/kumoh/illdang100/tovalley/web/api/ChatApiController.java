package kr.ac.kumoh.illdang100.tovalley.web.api;

import static kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.kafka.Message;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 API Document")
public class ChatApiController {

    private final ChatService chatService;
    //    private final SimpMessageSendingOperations template;
    private final SimpMessagingTemplate template;

    @PostMapping("/api/auth/chatroom")
//    @PostMapping("/api/chatroom")
    public ResponseEntity<?> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid final CreateNewChatRoomReqDto requestDto,
            BindingResult bindingResult) {

        CreateNewChatRoomRespDto respDto = chatService.createOrGetChatRoom(principalDetails.getMember().getId(),
                requestDto);
//        CreateNewChatRoomRespDto respDto = chatService.createOrGetChatRoom(1L, requestDto);
        if (respDto.isNew()) {
            return new ResponseEntity<>(new ResponseDto<>(1, "채팅방이 생성되었습니다", respDto), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponseDto<>(1, "기존 채팅방을 응답합니다", respDto), HttpStatus.OK);
        }
    }

    @GetMapping("/api/auth/chatroom")
    public ResponseEntity<?> findChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ChatRoomRespDto> result = chatService.getChatRoomSlice(principalDetails.getMember().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방 목록 조회를 성공했습니다", result), HttpStatus.OK);
    }

    // TODO: 채팅내역 조회
    @GetMapping("/api/auth/chat/messages/{chatRoomId}")
    public ResponseEntity<?> findChatMessages(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("chatRoomId") Long chatRoomId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ChatMessageListRespDto> result = chatService.getChatMessages(principalDetails.getMember().getId(), chatRoomId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅 메시지 목록 조회에 성공했습니다", result), HttpStatus.OK);
    }

    /*
    @MessageMapping은 Spring에서 제공하는 어노테이션으로,
    클라이언트에서 특정 주소로 메시지를 보냈을 때, 그 메시지를 받아 처리할 메서드에 붙이는 어노테이션
    이 어노테이션은 주로 WebSocket 통신에서 STOMP 메시지를 처리하는 데 사용된다.
     */
    // 사용자가 "/pub/chat/message"로 메시지를 전송하면 여기서 어떤 토픽의 메시지인지 구분하고 해당 토픽에 메시지를 전송한다.
    @MessageMapping("/chat/message") //websocket "/pub/chat/message"로 들어오는 메시지 처리
    public void sendMessage(@Payload Message message, @SessionAttribute(ChatUtil.MEMBER_ID) Long memberId) {
        log.debug("sendMessage 동작!!");
        template.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message); // /sub/chat/room/{roomId} - 구독
    }

    //
//    // TODO: 메시지 전송 후 callback
//    /*
//    애플리케이션 요구 사항에 따라 메시지 전송 후 콜백 처리 방법은 서버 측 자동 처리와 클라이언트 측 별도 요청 두 가지 존재
//     */
//    @PostMapping("/chatroom/notification")
//    public ResponseEntity<Message> sendNotification(@Valid @RequestBody Message message) {
//        Message savedMessage = chatService.sendNotificationAndSaveMessage(message);
//        return ResponseEntity.ok(savedMessage);
//    }
}
