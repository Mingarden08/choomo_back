package com.mithon.choomo.api.service;

import com.mithon.choomo.api.dto.req.ChatMessageReq;
import com.mithon.choomo.api.dto.res.ChatMessageRes;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomRepository chatRoomRepository,
                              ChatRoomMemberRepository chatRoomMemberRepository,
                              MemberRepository memberRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ChatMessageRes saveMessage(ChatMessageReq req) {
        ChatRoom chatRoom = chatRoomRepository.findById(req.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        Member sender = memberRepository.findById(req.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // 멤버 권한 확인
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(req.getChatRoomId(), req.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not in chat room"));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(req.getContent())
                .messageType(req.getMessageType())
                .isDeleted(false)
                .build();

        message = chatMessageRepository.save(message);

        log.info("Message saved: {}", message.getId());
        return convertToDto(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageRes> getChatMessages(Long chatRoomId, Long memberId) {
        // 멤버 권한 확인
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new RuntimeException("Access denied"));

        List<ChatMessage> messages = chatMessageRepository
                .findByChatRoomIdAndIsDeletedOrderByRegTimeDesc(chatRoomId, false);

        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long chatRoomId, Long memberId, Long messageId) {
        ChatRoomMember roomMember = chatRoomMemberRepository
                .findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new RuntimeException("Member not in chat room"));

        roomMember.setLastReadMessageId(messageId);
        chatRoomMemberRepository.save(roomMember);
    }

    @Transactional
    public void deleteMessage(Long messageId, Long memberId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(memberId)) {
            throw new RuntimeException("Not authorized to delete this message");
        }

        message.setIsDeleted(true);
        chatMessageRepository.save(message);
    }

    private ChatMessageRes convertToDto(ChatMessage message) {
        return ChatMessageRes.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .sentAt(message.getRegTime())
                .build();
    }
}