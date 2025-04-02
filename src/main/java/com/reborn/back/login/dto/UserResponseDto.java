package com.reborn.back.login.dto;

import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class UserResponseDto {
    @Schema(description = "UserInfoResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoResDto {
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "닉네임")
        private String name;
        @Schema(description = "프로필 사진")
        private String profileImage;
    }

    @Schema(description = "MainInfoResDto")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MainInfoResDto {
        @Schema(description = "이름")
        private String name;
        @Schema(description = "프로필 사진")
        private String profileImage;
        @Schema(description = "펫 정보")
        private List<mainInfoPet> petList;
        @Schema(description = "포스트")
        private List<AiPost> post;
    }

    @Schema(description = "MainInfoPet")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class mainInfoPet {
        private Pet pet;
        private boolean petCondition;
        private boolean todayRemind;
        private boolean todayRecord;
        private Integer fStep;
    }
}
