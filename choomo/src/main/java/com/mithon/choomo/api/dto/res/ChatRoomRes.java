package com.mithon.choomo.api.dto.res;

import com.mithon.choomo.api.entity.ChatMessage;
import com.mithon.choomo.api.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRes {

    @Schema(description = "채팅방 ID")
    private Long id;

    @Schema(description = "채팅방 이름")
    private String roomName;

    @Schema(description = "채팅방 타입")
    private ChatRoom.RoomType roomType;

    @Schema(description = "생성자 ID")
    private Long creatorId;

    @Schema(description = "생성자 이름")
    private String creatorName;

    @Schema(description = "참여 멤버 수")
    private Integer memberCount;

    @Schema(description = "마지막 메시지")
    private String lastMessage;

    @Schema(description = "마지막 메시지 시간")
    private LocalDateTime lastMessageTime;

    @Schema(description = "안읽은 메시지 수")
    private Long unreadCount;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;
}