package com.reborn.back.login.mapper;

import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.dto.UserResponseDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserConverter {
    public static User saveUser(UserRequestDto userReqDto) {
        return User.builder()
                .email(userReqDto.getEmail())
                .name(userReqDto.getUsername())
                .deviceToken(userReqDto.getDeviceToken())
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
                .email(user.getEmail())
                .name(user.getName().substring(user.getName().indexOf("}") + 1))
                .profileImage(user.getImg())
                .build();
    }

    public static UserResponseDto.MainInfoResDto mainDto(User user, List<UserResponseDto.mainInfoPet> petList, List<AiPost> recentPosts) {
        return UserResponseDto.MainInfoResDto.builder()
                .name(user.getName().substring(user.getName().indexOf("}") + 1))
                .profileImage(user.getImg())
                .petList(petList) // 서비스에서 처리된 petList 사용
                .post(recentPosts) // 서비스에서 처리된 최신 포스트 사용
                .build();
    }
}