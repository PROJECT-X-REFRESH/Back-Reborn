package com.reborn.back.review.recollection.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.recollection.dto.RemindDto;
import com.reborn.back.review.recollection.service.RemindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "remind", description = "remind 관련 API")
@RestController
@RequestMapping("/remind")
@RequiredArgsConstructor
public class RemindController {

    private final UserService userService;
    private final RemindService remindService;

    @Operation(summary = "remind 생성", description = "remind 생성하는 API")
    @PostMapping("/{petId}/create")
    public ApiResponse<Integer> createRemind(
            @PathVariable Integer petId,
            @RequestBody RemindDto.RemindReqDto remindDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Integer id = remindService.createRemind(petId, remindDto, user);
        return ApiResponse.onSuccess(SuccessCode.REMIND_CREATED, id);
    }

    @Operation(summary = "remind 수정", description = "remind 내용을 수정하는 API")
    @PutMapping("/{remindId}")
    public ApiResponse<RemindDto.RemindResDto> updateRemind(
            @PathVariable Integer remindId,
            @RequestBody RemindDto.RemindReqDto remindDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        RemindDto.RemindResDto res = remindService.updateRemind(remindId, remindDto, user);
        return ApiResponse.onSuccess(SuccessCode.REMIND_UPDATED, res);
    }

    @Operation(summary = "remind 목록 조회", description = "remind 목록을 조회하는 API")
    @PostMapping("/list/{petId}/{scrollPosition}/{fetchSize}")
    public ApiResponse<List<RemindDto.RemindResDto>> getListReminds(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int petId,
            @PathVariable int scrollPosition,
            @PathVariable int fetchSize
    ) {
        userService.findUserByUserName(customUserDetails.getUsername());
        List<RemindDto.RemindResDto> list = remindService.getRemindList(petId, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.REMIND_LIST_VIEW_SUCCESS, list);
    }

    @Operation(summary = "remind 상세 조회", description = "특정 remind 조회하는 API")
    @GetMapping("/{remindId}")
    public ApiResponse<RemindDto.RemindResDto> getRemind(
            @PathVariable Integer remindId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.findUserByUserName(customUserDetails.getUsername());
        RemindDto.RemindResDto res = remindService.getRemind(remindId);
        return ApiResponse.onSuccess(SuccessCode.REMIND_DETAIL_VIEW_SUCCESS, res);
    }

    @Operation(summary = "remind 삭제", description = "감정일기를 삭제하는 API (오늘만 가능)")
    @DeleteMapping("/{petId}/{remindId}")
    public ApiResponse<Boolean> deleteRemind(
            @PathVariable Integer petId,
            @PathVariable Integer remindId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        remindService.deleteRemind(remindId, user, petId);
        return ApiResponse.onSuccess(SuccessCode.REMIND_DELETED, true);
    }
}