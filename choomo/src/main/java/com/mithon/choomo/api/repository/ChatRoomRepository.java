package com.mithon.choomo.api.repository;

import com.mithon.choomo.api.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 회원이 참여중인 채팅방 조회
    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
            "JOIN cr.members m " +
            "WHERE m.member.id = :memberId AND m.isActive = true AND cr.isActive = true")
    List<ChatRoom> findActiveChatRoomsByMemberId(@Param("memberId") Long memberId);

    // 1:1 채팅방 찾기 (두 명의 멤버로)
    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE cr.roomType = 'DIRECT' " +
            "AND cr.isActive = true " +
            "AND cr.id IN (" +
            "  SELECT crm1.chatRoom.id FROM ChatRoomMember crm1 " +
            "  WHERE crm1.member.id = :memberId1 AND crm1.isActive = true" +
            ") " +
            "AND cr.id IN (" +
            "  SELECT crm2.chatRoom.id FROM ChatRoomMember crm2 " +
            "  WHERE crm2.member.id = :memberId2 AND crm2.isActive = true" +
            ")")
    Optional<ChatRoom> findDirectChatRoom(@Param("memberId1") Long memberId1,
                                          @Param("memberId2") Long memberId2);

    // 타입별 채팅방 조회
    List<ChatRoom> findByRoomTypeAndIsActive(ChatRoom.RoomType roomType, Boolean isActive);
}