package com.reborn.back.login.mapper;

import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.dto.UserResponseDto;
import com.reborn.back.review.recollection.service.RecordService;
import com.reborn.back.review.recollection.service.RemindService;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public static List<UserResponseDto.mainInfoPet> toMainInfoPetList(
            List<Pet> pets,
            String username,
            RemindService remindService,
            RecordService recordService
    ) {
        return pets.stream()
                .map(pet -> toMainInfoPet(pet, username, remindService, recordService))
                .toList();
    }

    private static UserResponseDto.mainInfoPet toMainInfoPet(
            Pet pet,
            String username,
            RemindService remindService,
            RecordService recordService
    ) {
        // Farewell이 없으면 살아있다(true), 있으면 죽었다(false)
        boolean isAlive = (pet.getFarewell() == null);

        // fStep: 살아있으면 0, 죽었으면 farewell.step
        Integer fStep = isAlive ? 0 : pet.getFarewell().getStep();
        Integer farewellId=null;
        // 살아있으면 remind/record 체크, 죽었으면 null 처리
        Boolean todayRemind = null;
        Boolean todayRecord = null;
        if (isAlive) {
            todayRemind = remindService.checkTodayRemind(username, pet);
            todayRecord = recordService.checkTodayRecord(username, pet);
        }
        else{
            farewellId = pet.getFarewell().getId();
        }

        return UserResponseDto.mainInfoPet.builder()
                .pet(toPetInfo(pet))
                .petCondition(isAlive)  // true or false
                .todayRemind(Boolean.TRUE.equals(todayRemind))
                .todayRecord(Boolean.TRUE.equals(todayRecord))
                .farewellId(farewellId)
                .fStep(fStep)
                .build();
    }

    private static UserResponseDto.PetInfoDto toPetInfo(Pet pet) {
        return UserResponseDto.PetInfoDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petCase(pet.getPetCase().name())   // enum → 문자열
                .birth(pet.getBirth())
                .death(pet.getDeath())
                .color(pet.getColor().name())
                .build();
    }

    public static UserResponseDto.MainInfoResDto mainDto(User user,
                                                         List<UserResponseDto.mainInfoPet> petList,
                                                         List<UserResponseDto.AiPostSimpleDto> recentPosts) {
        return UserResponseDto.MainInfoResDto.builder()
                .name(user.getName().substring(user.getName().indexOf("}") + 1))
                .profileImage(user.getImg())
                .petList(petList)
                .aiPost(recentPosts)
                .build();
    }
}