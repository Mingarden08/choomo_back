package com.mithon.choomo.api.controller;

import com.mithon.choomo.api.dto.req.ChatMessageReq;
import com.mithon.choomo.api.dto.res.ChatMessageRes;
import com.mithon.choomo.api.entity.ChatMessage;
import com.mithon.choomo.api.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatWebSocketController(SimpMessageSendingOperations messagingTemplate,
                                   ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    // 메시지 전송
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageReq message) {
        try {
            // DB에 메시지 저장
            ChatMessageRes savedMessage = chatMessageService.saveMessage(message);

            // 채팅방 타입에 따라 다른 destination으로 전송
            String destination = "/topic/chat/" + message.getChatRoomId();

            messagingTemplate.convertAndSend(destination, savedMessage);
            log.info("Message sent to {}: {}", destination, savedMessage.getId());
        } catch (Exception e) {
            log.error("Failed to send message: {}", e.getMessage());
        }
    }

    // 입장 알림
    @MessageMapping("/chat.join")
    public void joinChatRoom(@Payload ChatMessageReq message) {
        try {
            message.setMessageType(ChatMessage.MessageType.JOIN);
            ChatMessageRes savedMessage = chatMessageService.saveMessage(message);

            String destination = "/topic/chat/" + message.getChatRoomId();
            messagingTemplate.convertAndSend(destination, savedMessage);

            log.info("User {} joined chat room {}", message.getSenderId(), message.getChatRoomId());
        } catch (Exception e) {
            log.error("Failed to join chat room: {}", e.getMessage());
        }
    }

    // 퇴장 알림
    @MessageMapping("/chat.leave")
    public void leaveChatRoom(@Payload ChatMessageReq message) {
        try {
            message.setMessageType(ChatMessage.MessageType.LEAVE);
            ChatMessageRes savedMessage = chatMessageService.saveMessage(message);

            String destination = "/topic/chat/" + message.getChatRoomId();
            messagingTemplate.convertAndSend(destination, savedMessage);

            log.info("User {} left chat room {}", message.getSenderId(), message.getChatRoomId());
        } catch (Exception e) {
            log.error("Failed to leave chat room: {}", e.getMessage());
        }
    }

    // 타이핑 중 알림 (DB 저장 X)
    @MessageMapping("/chat.typing")
    public void typing(@Payload ChatMessageReq message) {
        String destination = "/topic/chat/" + message.getChatRoomId() + "/typing";
        messagingTemplate.convertAndSend(destination, message);
    }
}