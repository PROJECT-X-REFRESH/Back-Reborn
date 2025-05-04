package com.reborn.back.fcm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequestDto {
    private String token;

    private String title;

    private String body;

    @Builder(toBuilder = true)
    public FcmRequestDto(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
