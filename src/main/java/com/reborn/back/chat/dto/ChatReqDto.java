package com.reborn.back.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatReqDto {
    @Data
    @Builder
    @AllArgsConstructor
    public static class SendMessage {
        private Integer roomId;
        private boolean isFrom;
        private String Text;
    }
}
