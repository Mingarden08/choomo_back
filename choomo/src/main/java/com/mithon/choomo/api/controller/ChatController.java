package com.mithon.choomo.api.controller;

import com.mithon.choomo.api.dto.req.CreateChatRoomReq;
import com.mithon.choomo.api.dto.res.ChatMessageRes;
import com.mithon.choomo.api.dto.res.ChatRoomRes;
import com.mithon.choomo.api.dto.res.DataResponse;
import com.mithon.choomo.api.dto.res.ResponseCode;
import com.mithon.choomo.api.service.ChatMessageService;
import com.mithon.choomo.api.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "채팅 API")
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    public ChatController(ChatRoomService chatRoomService,
                          ChatMessageService chatMessageService) {
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    public ResponseEntity<DataResponse<ChatRoomRes>> createChatRoom(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CreateChatRoomReq req) {
        try {
            // TODO: JWT에서 사용자 ID 추출 (여기서는 임시로 req에서 받음)
            Long userId = 1L; // JWT에서 추출한 사용자 ID

            ChatRoomRes chatRoom = chatRoomService.createChatRoom(userId, req);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, chatRoom));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @GetMapping("/rooms")
    @Operation(summary = "내 채팅방 목록", description = "내가 참여중인 채팅방 목록을 조회합니다.")
    public ResponseEntity<DataResponse<List<ChatRoomRes>>> getMyChatRooms(
            @RequestHeader("Authorization") String authorization) {
        try {
            Long userId = 1L; // JWT에서 추출

            List<ChatRoomRes> chatRooms = chatRoomService.getMyChatRooms(userId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, chatRooms));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @GetMapping("/rooms/{chatRoomId}")
    @Operation(summary = "채팅방 상세", description = "채팅방 상세 정보를 조회합니다.")
    public ResponseEntity<DataResponse<ChatRoomRes>> getChatRoomDetail(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long chatRoomId) {
        try {
            Long userId = 1L; // JWT에서 추출

            ChatRoomRes chatRoom = chatRoomService.getChatRoomDetail(chatRoomId, userId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, chatRoom));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @GetMapping("/rooms/{chatRoomId}/messages")
    @Operation(summary = "채팅 메시지 조회", description = "채팅방의 메시지 목록을 조회합니다.")
    public ResponseEntity<DataResponse<List<ChatMessageRes>>> getChatMessages(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long chatRoomId) {
        try {
            Long userId = 1L; // JWT에서 추출

            List<ChatMessageRes> messages = chatMessageService.getChatMessages(chatRoomId, userId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, messages));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @PostMapping("/rooms/{chatRoomId}/read")
    @Operation(summary = "메시지 읽음 처리", description = "메시지를 읽음으로 표시합니다.")
    public ResponseEntity<DataResponse<Void>> markAsRead(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long chatRoomId,
            @RequestParam Long messageId) {
        try {
            Long userId = 1L; // JWT에서 추출

            chatMessageService.markAsRead(chatRoomId, userId, messageId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @DeleteMapping("/rooms/{chatRoomId}/leave")
    @Operation(summary = "채팅방 나가기", description = "채팅방에서 나갑니다.")
    public ResponseEntity<DataResponse<Void>> leaveChatRoom(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long chatRoomId) {
        try {
            Long userId = 1L; // JWT에서 추출

            chatRoomService.leaveChatRoom(chatRoomId, userId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }

    @DeleteMapping("/messages/{messageId}")
    @Operation(summary = "메시지 삭제", description = "메시지를 삭제합니다.")
    public ResponseEntity<DataResponse<Void>> deleteMessage(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long messageId) {
        try {
            Long userId = 1L; // JWT에서 추출

            chatMessageService.deleteMessage(messageId, userId);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_VALID, null));
        }
    }
}