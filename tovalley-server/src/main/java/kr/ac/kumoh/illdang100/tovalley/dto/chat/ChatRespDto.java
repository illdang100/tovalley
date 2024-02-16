package kr.ac.kumoh.illdang100.tovalley.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class ChatRespDto {

    @Data
    @AllArgsConstructor
    public static class CreateNewChatRoomRespDto {
        private boolean isNew;
        private Long chatRoomId;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ChatRoomRespDto {
        private Long chatRoomId;
        private String chatRoomTitle;
        private String otherUserProfileImage;
        private String otherUserNick;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdChatRoomDate;
        private String lastMessageContent;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
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

        public void changeLastMessage(String lastMessageContent, LocalDateTime lastMessageTime) {
            this.lastMessageContent = lastMessageContent;
            this.lastMessageTime = lastMessageTime;
        }
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessageListRespDto{
        private Long chatRoomId;
        private List<ChatMessageRespDto> chatMessages;
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessageRespDto {
        private String chatMessageId;
        private Long senderId;
        private boolean myMsg;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
//        private int reaCount;
    }
}
