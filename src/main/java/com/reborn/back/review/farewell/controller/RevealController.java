package com.reborn.back.review.farewell.controller;

import com.reborn.back.domain.review.farewell.Reveal;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.RevealRequestDto.RevealReqDto;
import com.reborn.back.review.farewell.dto.RevealResponseDto.DetailRevealDto;
import com.reborn.back.review.farewell.service.RevealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reveal", description = "reveal 관련 API")
@RestController
@RequestMapping("/farewell/{farewellId}/reveal")
@RequiredArgsConstructor
public class RevealController {

    private final RevealService revealService;
    private final UserService userService;

    //나의 감정 드러내기
    @Operation(summary = "Reveal 생성", description = "fstep에 따라 Reveal을 생성하는 API")
    @ApiResponses(value =  {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVEAL_2011", description = "나의 감정 드러내기 생성이 완료되었습니다.")
    })
    @PostMapping
    public ApiResponse<Integer> createReveal(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        Reveal reveal = revealService.createReveal(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REVEAL_CREATED, reveal.getId());
    }

    @Operation(summary = "컨텐츠 상태 변경", description = "Reveal 컨텐츠(feed, snack, walk)의 상태를 변경하는 API")
    @ApiResponses(value =  {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVEAL_2003", description = "컨텐츠가 완료되었습니다.")
    })
    @PatchMapping("/{activityType}")
    public ApiResponse<String> updateRevealActivity(
            @PathVariable Integer farewellId,
            @PathVariable String activityType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        revealService.updateRevealActivity(farewellId, activityType);
        return ApiResponse.onSuccess(SuccessCode.REVEAL_ACTIVITY_UPDATED, activityType);
    }

    @Operation(summary = "일기 작성", description = "일기를 작성하는 API(SUNNY, CLOUDY, RAINY)")
    @ApiResponses(value =  {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVEAL_2003", description = "일기 작성이 완료되었습니다.")
    })
    @PostMapping("/write")
    public ApiResponse<Boolean> write(
            @PathVariable Integer farewellId,
            @RequestBody RevealReqDto revealReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        revealService.writeReveal(farewellId, revealReqDto);

        return ApiResponse.onSuccess(SuccessCode.REVEAL_WRITE_COMPLETED, true);
    }

    @Operation(summary = "Reveal 진행 상황", description = "진행중인 Reveal의 상태를 전달하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVEAL_2002", description = "나의 감정 드러내기 조회가 완료되었습니다.")
    })
    @GetMapping("/view")
    public ApiResponse<DetailRevealDto> getDetailReveal(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        DetailRevealDto detail = revealService.getDetailReveal(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REVEAL_DETAIL_VIEW_SUCCESS, detail);
    }
}
