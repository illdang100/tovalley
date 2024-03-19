package kr.ac.kumoh.illdang100.tovalley.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessage;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatType;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Slice;

public class ChatRespDto {

    @Data
    @AllArgsConstructor
    public static class CreateNewChatRoomRespDto {
        private boolean isNew;
        private Long chatRoomId;
    }

    @Data
    @AllArgsConstructor
    public static class ChatRoomsRespDto {
        private String memberName;
        private List<ChatRoomRespDto> chatRooms;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ChatRoomRespDto {
        private Long chatRoomId;
        private String chatRoomTitle;
        private String otherUserProfileImage;
        private String otherUserNick;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdChatRoomDate;
        private String lastMessageContent;
        private long unReadMessageCount;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime lastMessageTime;

        public ChatRoomRespDto(Long chatRoomId, String chatRoomTitle, String otherUserProfileImage,
                               String otherUserNick,
                               LocalDateTime createdChatRoomDate) {
            this.chatRoomId = chatRoomId;
            this.chatRoomTitle = chatRoomTitle;
            this.otherUserProfileImage = otherUserProfileImage;
            this.otherUserNick = otherUserNick;
            this.createdChatRoomDate = createdChatRoomDate;
        }

        public ChatRoomRespDto(ChatRoom chatRoom, Member otherMember) {
            this.chatRoomId = chatRoom.getId();
            this.otherUserProfileImage = otherMember.getImageFile() != null ? otherMember.getImageFile().getStoreFileUrl() : null;
            this.otherUserNick = otherMember.getNickname();
            this.chatRoomTitle = otherUserNick.concat(" 와(과)의 채팅방입니다.");
            this.createdChatRoomDate = chatRoom.getCreatedDate();
        }

        public void changeLastMessage(String lastMessageContent, LocalDateTime lastMessageTime) {
            this.lastMessageContent = lastMessageContent;
            this.lastMessageTime = lastMessageTime;
        }

        public void changeUnReadMessageCount(long unReadMessageCount) {
            this.unReadMessageCount = unReadMessageCount;
        }
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessageListRespDto{
        private Long memberId;
        private Long chatRoomId;
        private Slice<ChatMessageRespDto> chatMessages;
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessageRespDto {
        private String chatMessageId;
        private Long senderId;
        private boolean myMsg;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private int readCount;
        private ChatType chatType; // 채팅 타입 필드('TEXT', 'IMAGE')
        private String imageUrl; // 이미지 URL

        public ChatMessageRespDto(ChatMessage chatMessage, Long memberId) {
            this.chatMessageId = chatMessage.getId();
            this.senderId = chatMessage.getSenderId();
            this.myMsg = chatMessage.getSenderId().equals(memberId);
            this.content = chatMessage.getContent();
            this.createdAt = ChatUtil.convertZdtStringToLocalDateTime(chatMessage.getCreatedAt());
            this.readCount = chatMessage.getReadCount();
            this.chatType = chatMessage.getChatType();
            this.imageUrl = chatMessage.getImageUrl();
        }
    }
}
