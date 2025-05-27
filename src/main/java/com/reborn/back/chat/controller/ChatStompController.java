package com.reborn.back.chat.controller;

import com.reborn.back.chat.dto.ChatReqDto;
import com.reborn.back.chat.dto.ChatResDto;
import com.reborn.back.chat.service.ChatService;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.auth.jwt.JwtTokenUtils;
import com.reborn.back.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatService chatService;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    @MessageMapping("/chat/send/{chatId}")
    @SendTo("/sub/chat/{chatId}")
    public ChatResDto.MessageResponse sendMessage(
            @DestinationVariable Integer chatId,
            @Payload ChatReqDto.SendMessage payload,
            @Header("Authorization") String authHeader) {
            System.out.println("🔥 메시지 메서드 진입함");
        try {
            System.out.println("DEBUG - chatId: " + chatId);
            System.out.println("DEBUG - payload: " + payload);
            System.out.println("DEBUG - authHeader: " + authHeader);

            String token = authHeader.replace("Bearer ", "");
            String username = jwtTokenUtils.parseClaims(token).getSubject();
            User user = userService.findUserByUserName(username);

            return chatService.writeMessage(user, chatId, payload.getText());

        } catch (Exception e) {
            System.err.println("🔥 WebSocket 예외 발생: " + e.getMessage());
            e.printStackTrace(); // 💥 Stack trace 무조건 출력
            throw e;
        }
    }
}
