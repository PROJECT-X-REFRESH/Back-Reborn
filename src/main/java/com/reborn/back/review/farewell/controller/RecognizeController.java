package com.reborn.back.review.farewell.controller;


import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.global.utils.GCPMap.PlaceResponseDto;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.RecognizeRequestDto;
import com.reborn.back.review.farewell.dto.RecognizeResponseDto;
import com.reborn.back.review.farewell.service.FarewellService;
import com.reborn.back.review.farewell.service.RecognizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "recognize", description = "recognize 관련 API")
@RestController
@RequestMapping("/farewell/{farewellId}/recognize")
@RequiredArgsConstructor
public class RecognizeController {
    private final RecognizeService recognizeService;
    private final UserService userService;
    private final FarewellService farewellService;

    @Operation(summary = "나의 상태 알아보기 생성", description = "나의 상태 알아보기를 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2011", description = "나의 상태 알아보기 생성이 완료되었습니다.")
    })
    @PostMapping("/create")
    public ApiResponse<Boolean> createRecognize(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        recognizeService.createRecognize(farewellId);
        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_CREATED, true);
    }

    @Operation(summary = "컨텐츠 상태 변경", description = "Recognize 컨텐츠(feed, snack, walk)의 상태를 변경하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2003", description = "컨텐츠가 완료되었습니다.")
    })
    @PatchMapping("/{activityType}")
    public ApiResponse<String> updateRecognizeActivity(
            @PathVariable Integer farewellId,
            @PathVariable String activityType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        recognizeService.updateRecognizeActivity(farewellId, activityType);
        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_ACTIVITY_UPDATED, activityType);
    }

    @Operation(summary = "자가진단하기", description = "자가진단 점수를 저장하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2012", description = "자가진단 점수를 저장이 완료되었습니다.")
    })
    @PostMapping("/score")
    public ApiResponse<Boolean> score(
            @PathVariable Integer farewellId,
            @RequestBody RecognizeRequestDto.RecognizeReqDto recognizeDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        recognizeService.saveScore(farewellId, recognizeDto);

        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_SAVE_COMPLETED, true);
    }

    @Operation(summary = "주변 상담소 조회", description = "사용자 위치를 기반으로 주변 상담소 목록을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2001", description = "주변 상담소 조회가 완료되었습니다.")
    })
    @GetMapping("/nearby")
    public ApiResponse<List<PlaceResponseDto>> getNearbyCounselingCenters(
            @PathVariable Integer farewellId,
            @RequestParam double lat,
            @RequestParam double lng,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        List<PlaceResponseDto> places = recognizeService.getNearbyCounselingCenters(lat, lng);
        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_NEARBY_SUCCESS, places);
    }

    @Operation(summary = "Recognize 진행 상황", description = "진행중인 Recognize의 상태를 전달하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2002", description = "나의 상태 알아보기 조회가 완료되었습니다.")
    })
    @GetMapping("/view")
    public ApiResponse<RecognizeResponseDto.DetailRecognizeDto> getDetailRecognize(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        RecognizeResponseDto.DetailRecognizeDto detail = recognizeService.getDetailRecognize(farewellId);
        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_DETAIL_VIEW_SUCCESS, detail);
    }
}
