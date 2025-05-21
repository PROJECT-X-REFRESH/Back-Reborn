package com.reborn.back.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ChatRoomDto {
    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateRoomRequest {
        private String partnerUsername;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class RoomList {
        private Integer roomId;
        private String partnerUserId;
        private String partnerNickname;
        private String lastMsg;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime lastTime;
    }
}
