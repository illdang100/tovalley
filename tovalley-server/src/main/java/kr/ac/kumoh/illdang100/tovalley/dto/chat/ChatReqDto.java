package kr.ac.kumoh.illdang100.tovalley.dto.chat;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class ChatReqDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CreateNewChatRoomReqDto {

        @NotEmpty
        @Length(max = 20)
        private String recipientNick;
    }
}
