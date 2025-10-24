package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_name", length = 100)
    @Schema(description = "채팅방 이름 (단체 채팅방일 경우)")
    private String roomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    @Schema(description = "채팅방 타입 (DIRECT: 1:1, GROUP: 단체)")
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    @Schema(description = "채팅방 생성자")
    private Member creator;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatRoomMember> members = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    @Schema(description = "활성화 여부")
    private Boolean isActive = true;

    public enum RoomType {
        DIRECT,  // 1:1 채팅
        GROUP    // 단체 채팅
    }

    // 멤버 추가
    public void addMember(ChatRoomMember member) {
        members.add(member);
        member.setChatRoom(this);
    }

    // 멤버 제거
    public void removeMember(ChatRoomMember member) {
        members.remove(member);
        member.setChatRoom(null);
    }
}