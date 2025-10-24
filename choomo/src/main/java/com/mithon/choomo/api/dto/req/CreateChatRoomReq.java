package com.mithon.choomo.api.dto.req;

import com.mithon.choomo.api.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomReq {

    @Schema(description = "채팅방 타입 (DIRECT, GROUP)")
    private ChatRoom.RoomType roomType;

    @Schema(description = "채팅방 이름 (단체 채팅일 경우)")
    private String roomName;

    @Schema(description = "참여할 멤버 ID 목록")
    private List<Long> memberIds;
}