package kr.ac.kumoh.illdang100.tovalley.web.api;

import static kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomsRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.chat.ChatService;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 API Document")
public class ChatApiController {

    private final ChatService chatService;
    private final S3Service s3Service;

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
    public ResponseEntity<?> findChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ChatRoomsRespDto result = chatService.getChatRooms(principalDetails.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅방 목록 조회를 성공했습니다", result), HttpStatus.OK);
    }

    @GetMapping("/api/auth/chat/messages/{chatRoomId}")
    public ResponseEntity<?> findChatMessages(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(required = false) String cursor,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ChatMessageListRespDto result
                = chatService.getChatMessages(principalDetails.getMember().getId(), chatRoomId, cursor, pageable);

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
    
    @PostMapping(value = "/api/auth/chat/images/upload")
    public ResponseEntity<?> saveChatImage(@RequestPart(value = "image", required = false) MultipartFile chatImage) {

        // chatImage가 null인 경우 처리
        if (chatImage == null || chatImage.isEmpty()) {
            return new ResponseEntity<>(new ResponseDto<>(-1, "업로드할 이미지가 제공되지 않았습니다", null), HttpStatus.BAD_REQUEST);
        }

        try {
            ImageFile chatImageFile = s3Service.upload(chatImage, FileRootPathVO.CHAT_PATH);
            return new ResponseEntity<>(new ResponseDto<>(1, "채팅 메시지 이미지가 저장되었습니다", chatImageFile), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }
}
