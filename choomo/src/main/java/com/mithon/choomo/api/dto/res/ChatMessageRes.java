package com.mithon.choomo.api.dto.res;

import com.mithon.choomo.api.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRes {

    @Schema(description = "메시지 ID")
    private Long id;

    @Schema(description = "채팅방 ID")
    private Long chatRoomId;

    @Schema(description = "발신자 ID")
    private Long senderId;

    @Schema(description = "발신자 이름")
    private String senderName;

    @Schema(description = "메시지 내용")
    private String content;

    @Schema(description = "메시지 타입")
    private ChatMessage.MessageType messageType;

    @Schema(description = "전송 시간")
    private LocalDateTime sentAt;
}