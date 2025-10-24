package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @Schema(description = "채팅방")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @Schema(description = "발신자")
    private Member sender;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "메시지 내용")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    @Schema(description = "메시지 타입")
    private MessageType messageType;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    @Schema(description = "삭제 여부")
    private Boolean isDeleted = false;

    public enum MessageType {
        CHAT,    // 일반 채팅
        JOIN,    // 입장 알림
        LEAVE,   // 퇴장 알림
        SYSTEM   // 시스템 메시지
    }
}