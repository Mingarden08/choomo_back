package com.mithon.choomo.api.repository;

import com.mithon.choomo.api.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    // 특정 채팅방의 활성 멤버 조회
    List<ChatRoomMember> findByChatRoomIdAndIsActive(Long chatRoomId, Boolean isActive);

    // 특정 회원의 특정 채팅방 참여 정보
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    // 채팅방의 멤버 수
    long countByChatRoomIdAndIsActive(Long chatRoomId, Boolean isActive);
}