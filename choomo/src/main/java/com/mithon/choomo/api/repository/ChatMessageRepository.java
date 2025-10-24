package com.mithon.choomo.api.repository;

import com.mithon.choomo.api.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 메시지 조회 (최신순)
    List<ChatMessage> findByChatRoomIdAndIsDeletedOrderByRegTimeDesc(Long chatRoomId, Boolean isDeleted);

    // 특정 채팅방의 메시지 조회 (페이징)
    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE cm.chatRoom.id = :chatRoomId " +
            "AND cm.isDeleted = false " +
            "ORDER BY cm.regTime DESC")
    List<ChatMessage> findRecentMessages(@Param("chatRoomId") Long chatRoomId);

    // 안읽은 메시지 수 조회
    @Query("SELECT COUNT(cm) FROM ChatMessage cm " +
            "WHERE cm.chatRoom.id = :chatRoomId " +
            "AND cm.id > :lastReadMessageId " +
            "AND cm.isDeleted = false")
    long countUnreadMessages(@Param("chatRoomId") Long chatRoomId,
                             @Param("lastReadMessageId") Long lastReadMessageId);
}