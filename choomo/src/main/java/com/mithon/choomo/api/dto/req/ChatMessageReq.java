package com.mithon.choomo.api.dto.req;

import com.mithon.choomo.api.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {

    @Schema(description = "채팅방 ID")
    private Long chatRoomId;

    @Schema(description = "발신자 ID")
    private Long senderId;

    @Schema(description = "메시지 내용")
    private String content;

    @Schema(description = "메시지 타입")
    private ChatMessage.MessageType messageType;
}