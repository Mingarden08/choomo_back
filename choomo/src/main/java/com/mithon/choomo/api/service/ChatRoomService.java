package com.mithon.choomo.api.service;

import com.mithon.choomo.api.dto.req.CreateChatRoomReq;
import com.mithon.choomo.api.dto.res.ChatRoomRes;
import com.mithon.choomo.api.entity.ChatMessage;
import com.mithon.choomo.api.entity.ChatRoom;
import com.mithon.choomo.api.entity.ChatRoomMember;
import com.mithon.choomo.api.entity.Member;
import com.mithon.choomo.api.repository.ChatMessageRepository;
import com.mithon.choomo.api.repository.ChatRoomMemberRepository;
import com.mithon.choomo.api.repository.ChatRoomRepository;
import com.mithon.choomo.api.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           ChatRoomMemberRepository chatRoomMemberRepository,
                           ChatMessageRepository chatMessageRepository,
                           MemberRepository memberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ChatRoomRes createChatRoom(Long creatorId, CreateChatRoomReq req) {
        Member creator = memberRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // 1:1 채팅방인 경우 기존 방 확인
        if (req.getRoomType() == ChatRoom.RoomType.DIRECT) {
            if (req.getMemberIds().size() != 1) {
                throw new RuntimeException("Direct chat requires exactly one other member");
            }

            Long otherMemberId = req.getMemberIds().get(0);
            Optional<ChatRoom> existingRoom = chatRoomRepository
                    .findDirectChatRoom(creatorId, otherMemberId);

            if (existingRoom.isPresent()) {
                return convertToDto(existingRoom.get(), creatorId);
            }
        }

        // 새 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(req.getRoomName())
                .roomType(req.getRoomType())
                .creator(creator)
                .isActive(true)
                .build();

        chatRoom = chatRoomRepository.save(chatRoom);

        // 생성자 추가
        addMemberToRoom(chatRoom, creator);

        // 다른 멤버들 추가
        for (Long memberId : req.getMemberIds()) {
            if (!memberId.equals(creatorId)) {
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));
                addMemberToRoom(chatRoom, member);
            }
        }

        log.info("Chat room created: {}", chatRoom.getId());
        return convertToDto(chatRoom, creatorId);
    }

    private void addMemberToRoom(ChatRoom chatRoom, Member member) {
        ChatRoomMember roomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .isActive(true)
                .build();
        chatRoomMemberRepository.save(roomMember);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomRes> getMyChatRooms(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findActiveChatRoomsByMemberId(memberId);
        return chatRooms.stream()
                .map(room -> convertToDto(room, memberId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoomRes getChatRoomDetail(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        // 멤버 권한 확인
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new RuntimeException("Access denied"));

        return convertToDto(chatRoom, memberId);
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long memberId) {
        ChatRoomMember roomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new RuntimeException("Member not in chat room"));

        roomMember.setIsActive(false);
        chatRoomMemberRepository.save(roomMember);

        log.info("Member {} left chat room {}", memberId, chatRoomId);
    }

    private ChatRoomRes convertToDto(ChatRoom chatRoom, Long currentMemberId) {
        List<ChatRoomMember> activeMembers = chatRoomMemberRepository
                .findByChatRoomIdAndIsActive(chatRoom.getId(), true);

        // 마지막 메시지 조회
        List<ChatMessage> recentMessages = chatMessageRepository
                .findRecentMessages(chatRoom.getId());

        ChatMessage lastMessage = recentMessages.isEmpty() ? null : recentMessages.get(0);

        // 안읽은 메시지 수 계산
        Optional<ChatRoomMember> currentMember = chatRoomMemberRepository
                .findByChatRoomIdAndMemberId(chatRoom.getId(), currentMemberId);

        Long unreadCount = 0L;
        if (currentMember.isPresent() && currentMember.get().getLastReadMessageId() != null) {
            unreadCount = chatMessageRepository.countUnreadMessages(
                    chatRoom.getId(),
                    currentMember.get().getLastReadMessageId()
            );
        }

        // 1:1 채팅방 이름 설정 (상대방 이름)
        String roomName = chatRoom.getRoomName();
        if (chatRoom.getRoomType() == ChatRoom.RoomType.DIRECT) {
            roomName = activeMembers.stream()
                    .filter(m -> !m.getMember().getId().equals(currentMemberId))
                    .map(m -> m.getMember().getName())
                    .findFirst()
                    .orElse("Unknown");
        }

        return ChatRoomRes.builder()
                .id(chatRoom.getId())
                .roomName(roomName)
                .roomType(chatRoom.getRoomType())
                .creatorId(chatRoom.getCreator().getId())
                .creatorName(chatRoom.getCreator().getName())
                .memberCount(activeMembers.size())
                .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                .lastMessageTime(lastMessage != null ? lastMessage.getRegTime() : null)
                .unreadCount(unreadCount)
                .createdAt(chatRoom.getRegTime())
                .build();
    }
}