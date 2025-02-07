package com.reborn.back.login.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.auth.jwt.CustomUserDetails;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.dto.UserResponseDto;
import com.reborn.back.login.mapper.UserConverter;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "회원", description = "회원 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AmazonS3Manager amazonS3Manager;

    @Operation(summary = "로그아웃", description = "로그아웃하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2001", description = "로그아웃 되었습니다."),
    })
    @DeleteMapping("/logout")
    public ApiResponse<Integer> logout(HttpServletRequest request) {
        userService.logout(request);
        // acess token 강제 만료 시키는 로직(블랙리스트 방식) 작성 필요
        return ApiResponse.onSuccess(SuccessCode.USER_LOGOUT_SUCCESS, 1);
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2002", description = "토큰 재발급이 완료되었습니다."),
    })
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(
            HttpServletRequest request
    ) {
        JwtDto jwt = userService.reissue(request);
        return ApiResponse.onSuccess(SuccessCode.USER_REISSUE_SUCCESS, jwt);
    }

    @Operation(summary = "회원탈퇴", description = "회원 탈퇴하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2003", description = "회원탈퇴가 완료되었습니다."),
    })
    @DeleteMapping("/me")
    public ApiResponse<Integer> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ApiResponse.onSuccess(SuccessCode.USER_DELETE_SUCCESS, 1);
    }

    @Operation(summary = "프로필 사진 첨부", description = "프로필 사진을 첨부하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2004", description = "프로필 사진 첨부가 완료되었습니다.")
    })
    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Boolean> createProfileImage(
            @RequestPart(value = "profile") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        String dirName = "profile/";
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        userService.createProfileImage(dirName, file, user);

        return ApiResponse.onSuccess(SuccessCode.USER_PROFILE_IMAGE_UPDATED, true);
    }

    @Operation(summary = "배경 사진 첨부", description = "배경 사진을 첨부하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2005", description = "배경 사진 첨부가 완료되었습니다.")
    })
    @PostMapping(value = "/background-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Boolean> createBackgroundImage(
            @RequestPart(value = "background") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        String dirName = "background/";
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        userService.createBackgroundImage(dirName, file, user);

        return ApiResponse.onSuccess(SuccessCode.USER_BACKGROUND_IMAGE_UPDATED, true);
    }

    @Operation(summary = "사진 삭제", description = "s3에 업로드 된 사진을 삭제하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE_2001", description = "사진 삭제가 완료되었습니다.")
    })
    @DeleteMapping(value = "/delete-image")
    public ApiResponse<Boolean> deleteImage(
            @RequestParam("filePath") String filePath,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        amazonS3Manager.deleteFile(filePath);

        return ApiResponse.onSuccess(SuccessCode.FILE_DELETE_SUCCESS, true);
    }

    @Operation(summary = "프로필 사진 열람", description = "프로필 사진을 url로 열람하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2006", description = "프로필 사진 열람이 완료되었습니다.")
    })
    @GetMapping(value = "/show-profile-image")
    public ApiResponse<String> showProfileImage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        String imageUrl = userService.showProfileImage(user);

        return ApiResponse.onSuccess(SuccessCode.USER_PROFILE_IMAGE_BROWSE, imageUrl);
    }

    @Operation(summary = "배경 사진 열람", description = "배경 사진을 url로 열람하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2007", description = "배경 사진 열람이 완료되었습니다.")
    })
    @GetMapping(value = "/show-background-image")
    public ApiResponse<String> showBackgroundImage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        String imageUrl = userService.showBackgroundImage(user);

        return ApiResponse.onSuccess(SuccessCode.USER_BACKGROUND_IMAGE_BROWSE, imageUrl);
    }

    @Operation(summary = "내 정보 열람", description = "내 정보(since, email) 열람하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2008", description = "유저 정보 열람이 완료되었습니다.")
    })
    @GetMapping(value = "/info")
    public ApiResponse<UserResponseDto.UserInfoResDto> showInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        return ApiResponse.onSuccess(SuccessCode.USER_INFO_SUCCESS, UserConverter.infoDto(user));
    }

    @Operation(summary = "닉네임 입력", description = "중복 안되는 닉네임을 입력받는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2009", description = "닉네임 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/nickname")
    public ApiResponse<Boolean> nickname(
            @RequestBody UserRequestDto.UserNicknameReqDto nicknameReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        userService.saveNickname(nicknameReqDto, user);

        return ApiResponse.onSuccess(SuccessCode.USER_NICKNAME_SUCCESS, true);
    }

    @Operation(summary = "메인 화면의 닉네임, 사진", description = "메인 화면에 띄울 정보들을 보이는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2010", description = "닉네임, 사진 전달이 완료되었습니다.")
    })
    @GetMapping(value = "/main")
    public ApiResponse<UserResponseDto.MainInfoResDto> mainInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        return ApiResponse.onSuccess(SuccessCode.MAIN_INFO_SUCCESS, UserConverter.mainDto(user));
    }

}