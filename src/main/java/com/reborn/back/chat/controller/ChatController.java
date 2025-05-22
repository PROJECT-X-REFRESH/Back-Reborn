package com.reborn.back.chat.controller;

import com.reborn.back.chat.dto.ChatReqDto;
import com.reborn.back.chat.dto.ChatResDto;
import com.reborn.back.chat.dto.ChatRoomDto;
import com.reborn.back.chat.service.ChatService;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.*;

import java.util.List;

@Tag(name = "chat", description = "chat 관련 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    // 0. 목록 가져오기
    // post
    @Operation(summary = "전체 채팅 목록 조회", description = "채팅 목록 조회")
    @Parameters({
            @Parameter(name = "scrollPosition", description = "가져올 데이터의 시작 위치 (0부터 시작)"),
            @Parameter(name = "fetchSize", description = "한 번에 불러올 채팅 개수")
    })
    @GetMapping("/list")
    public ApiResponse<List<ChatRoomDto.RoomList>> getListChat(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "50") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(
                SuccessCode.CHAT_LIST_VIEW_SUCESS,
                chatService.getRoomList(user, scrollPosition, fetchSize)
        );
    }


    // 1) handshake – 방이 없으면 생성
    @Operation(summary = "채팅방 생성/조회", description = "상대 UID 로 handshake")
    @GetMapping("/handshake/{partnerId}")
    public ApiResponse<ChatRoomDto.RoomList> handshake(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable String partnerId) {

        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(
                SuccessCode.CHAT_ROOM_CREATE,
                chatService.handshake(user, partnerId)
        );
    }

    // 2. 채팅방 세부 조회
    @Operation(summary = "채팅방 불러오기", description = "채팅방 내용 상세 조회 API")
    @Parameters({
            @Parameter(name = "scrollPosition", description = "가져올 데이터의 시작 위치 (0부터 시작)"),
            @Parameter(name = "fetchSize", description = "한 번에 불러올 채팅 개수")
    })
    @GetMapping("/{chatId}")
    public ApiResponse<List<ChatResDto.MessageResponse>> getChatDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Integer chatId,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "50") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(
                SuccessCode.CHAT_DETAIL_VIEW_SUCESS,
                chatService.getChatDetail(user, chatId, scrollPosition, fetchSize)
        );
    }


    // 3. 메세지 전송, STOMP
    @MessageMapping("/chat.sendMessage/{chatId}")
    public ChatResDto.MessageResponse sendMessage(
            @DestinationVariable Integer chatId,
            ChatReqDto.SendMessage payload,
            @AuthenticationPrincipal CustomUserDetails customUserDetail) {
        User user = userService.findUserByUserName(customUserDetail.getUsername());
        return chatService.writeMessage(user, chatId, payload.getText());
    }

    // 4. 방 나가기
    @Operation(summary = "방나가기", description = "방 나가기 API")
    @DeleteMapping("/{chatId}")
    public ApiResponse<Boolean> deleteBoard(
            @PathVariable Integer chatId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        chatService.leaveRoom(user, chatId);
        return ApiResponse.onSuccess(SuccessCode.CHAT_DELETED, true);
    }


}
