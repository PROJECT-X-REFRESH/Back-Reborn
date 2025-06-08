package com.reborn.back.review.recollection.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.recollection.dto.RecollectDto;
import com.reborn.back.review.recollection.service.RecollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "recollection", description = "record & remind 동시 호출")
@RestController
@RequestMapping("/recollection")
@RequiredArgsConstructor
public class RecollectionController {
    private final UserService userService;
    private final RecollectionService recollectionService;

    @Operation(summary = "주간 상황 조회", description = "이번주 추억쌓기 목록을 조회하는 API")
    @GetMapping("/week/{petId}")
    public ApiResponse<List<RecollectDto.RecollectionDto>> getWeeksRecollection(
            @PathVariable Integer petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<RecollectDto.RecollectionDto> recollections = recollectionService.getThisWeekList(user, petId);
        return ApiResponse.onSuccess(SuccessCode.RECOLLECTION_WEEK_VIEW_SUCCESS, recollections);
    }

    @Operation(summary = "일간 상황 조회", description = "오늘의 추억쌓기를 조회하는 API")
    @GetMapping("/today/{petId}")
    public ApiResponse<RecollectDto.TodayRecollctDto> getTodaysRecollection(
            @PathVariable Integer petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        RecollectDto.TodayRecollctDto recollections = recollectionService.getTodayList(user, petId);
        return ApiResponse.onSuccess(SuccessCode.RECOLLECTION_WEEK_VIEW_SUCCESS, recollections);
    }

    @Operation(summary = "추억 앨범 ID 조회 (존재 시)")
    @GetMapping("/id/{petId}")
    public ApiResponse<Integer> getRecollectionId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Integer petId) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Integer result = recollectionService.checkRecollection(user, petId);
        if (result == null) {
            return ApiResponse.onSuccess(SuccessCode.RECOLLECTION_NOT_FOUND, null);
        }
        return ApiResponse.onSuccess(SuccessCode.RECOLLECTION_ALBUM_EXIST, result);
    }
}
