package com.reborn.back.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
public class ChatResDto {

    @Data
    @Builder
    @AllArgsConstructor
    public static class MessageResponse {
        private Integer messageId;
        private Boolean mine;
        private String text;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime sentAt;
    }
}
