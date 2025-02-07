package com.reborn.back.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class UserResponseDto {
    @Schema(description = "UserInfoResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoResDto {

        @Schema(description = "가입시기")
        private LocalDateTime since;

        @Schema(description = "이메일")
        private String email;

        @Schema(description = "닉네임")
        private String nickname;
    }

    @Schema(description = "MainInfoResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MainInfoResDto {

        @Schema(description = "프로필 사진")
        private String profileImage;

        @Schema(description = "닉네임")
        private String nickname;
    }
}
