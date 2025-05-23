package com.reborn.back.review.farewell.controller;


import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.CounselingCenterDto;
import com.reborn.back.review.farewell.dto.RecognizeRequestDto.RecognizeReqDto;
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

    @Operation(summary = "나의 상태 알아보기 생성", description = "나의 상태 알아보기를 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECOGNIZE_2011", description = "나의 상태 알아보기 생성이 완료되었습니다.")
    })
    @PostMapping("/create")
    public ApiResponse<Boolean> createComment(
            @PathVariable Integer farewellId,
            @RequestBody RecognizeReqDto recognizeDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        recognizeService.createRecognize(farewellId, recognizeDto);

        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_CREATED, true);
    }

    @Operation(summary = "주변 상담소 + HIRA 평가정보 조회")
    @GetMapping("/nearby")
    public ApiResponse<List<CounselingCenterDto>> getNearbyCounselingCenters(
            @PathVariable Integer farewellId,
            @RequestParam double lat,
            @RequestParam double lng,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // (사용자·farewell 검증 로직 그대로)

        List<CounselingCenterDto> result =
                recognizeService.getCounselingCentersWithGrade(lat, lng);

        return ApiResponse.onSuccess(SuccessCode.RECOGNIZE_NEARBY_SUCCESS, result);
    }
}
