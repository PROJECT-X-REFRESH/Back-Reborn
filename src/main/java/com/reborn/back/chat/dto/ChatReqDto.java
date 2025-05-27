package com.reborn.back.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatReqDto {
    @Data
    @Builder
    @JsonPOJOBuilder(withPrefix = "")
    @AllArgsConstructor
    public static class SendMessage {
        private String text;
    }
}
