package com.reborn.back.login.mapper;

import com.reborn.back.domain.user.User;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.dto.UserResponseDto;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UserConverter {
    public static User saveUser(UserRequestDto.UserReqDto userReqDto) {
        return User.builder()
                .email(userReqDto.getEmail())
                .username(userReqDto.getUsername())
                .provider(userReqDto.getProvider())
                .nickname(userReqDto.getNickname())
                .deviceToken(userReqDto.getDeviceToken())
                .contentPetId(null)
                .profileImage(null)
                .backgroundImage(null)
                .build();
    }

    public static JwtDto jwtDto(String access, String refresh, String signIn) {
        return JwtDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .signIn(signIn)
                .build();
    }

    public static UserResponseDto.UserInfoResDto infoDto(User user) {
        return UserResponseDto.UserInfoResDto.builder()
                .since(user.getCreatedAt())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public static UserResponseDto.MainInfoResDto mainDto(User user) {
        return UserResponseDto.MainInfoResDto.builder()
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .build();
    }
}