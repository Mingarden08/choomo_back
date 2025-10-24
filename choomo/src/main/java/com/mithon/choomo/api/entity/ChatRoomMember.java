package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_room_member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @Schema(description = "채팅방")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Schema(description = "참여 멤버")
    private Member member;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    @Schema(description = "활성 상태 (나가기 시 false)")
    private Boolean isActive = true;

    @Column(name = "last_read_message_id")
    @Schema(description = "마지막으로 읽은 메시지 ID")
    private Long lastReadMessageId;
}
