package com.reborn.back.review.farewell.controller;

import com.reborn.back.domain.review.farewell.Rebirth;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.RebirthRequestDto.RebirthReqDto;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.DetailRebirthDto;
import com.reborn.back.review.farewell.service.FarewellService;
import com.reborn.back.review.farewell.service.RebirthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "rebirth", description = "rebirth 관련 API")
@RestController
@RequestMapping("/farewell/{farewellId}/rebirth")
@RequiredArgsConstructor
public class RebirthController {

    private final RebirthService rebirthService;
    private final UserService userService;
    private final FarewellService farewellService;

    // 반려동물과 건강한 작별하기
    @Operation(summary = "Rebirth 생성", description = "fstep에 따라 Rebirth을 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Rebirth_2011", description = "반려동물과 건강한 작별하기 생성이 완료되었습니다.")
    })
    @PostMapping
    public ApiResponse<Integer> createRebirth(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        Rebirth rebirth = rebirthService.createRebirth(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REBIRTH_CREATED, rebirth.getId());
    }

    @Operation(summary = "컨텐츠 상태 변경", description = "Rebirth 컨텐츠(wash, clothes, ribbon{yribbon -> 노란색, bribbon -> 검정색}, outro)의 상태를 변경하는 API")
    @ApiResponses(value =  {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REBIRTH_2003", description = "컨텐츠가 완료되었습니다.")
    })
    @PatchMapping("/{activityType}")
    public ApiResponse<String> updateRebirthActivity(
            @PathVariable Integer farewellId,
            @PathVariable String activityType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        rebirthService.updateRebirthActivity(farewellId, activityType);
        return ApiResponse.onSuccess(SuccessCode.REBIRTH_ACTIVITY_UPDATED, activityType);
    }

    @Operation(summary = "반려동물 편지 저장", description = "반려동물 편지 저장하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REBIRTH_2003", description = "편지 저장이 완료되었습니다.")
    })
    @PostMapping("/write")
    public ApiResponse<Boolean> write(
            @PathVariable Integer farewellId,
            @RequestBody RebirthReqDto rebirthRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        rebirthService.writeRebirth(farewellId, rebirthRequestDto);

        return ApiResponse.onSuccess(SuccessCode.REBIRTH_WRITE_COMPLETED, true);
    }

    @Operation(summary = "Rebirth 진행 상황", description = "진행중인 Rebirth의 상태를 전달하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REBIRTH_2002", description = "나의 감정 드러내기 조회가 완료되었습니다.")
    })
    @GetMapping("/view")
    public ApiResponse<DetailRebirthDto> getDetailRebirth(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        DetailRebirthDto detail = rebirthService.getDetailRebirth(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REBIRTH_DETAIL_VIEW_SUCCESS, detail);
    }
}
