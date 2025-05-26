package com.reborn.back.review.farewell.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.FarewellResponseDto;
import com.reborn.back.review.farewell.dto.FarewellSimpleDto;
import com.reborn.back.review.farewell.dto.RememberResponseDto.ReviewRememberDto;
import com.reborn.back.review.farewell.dto.RevealResponseDto.ReviewRevealDto;
import com.reborn.back.review.farewell.service.FarewellService;
import com.reborn.back.review.farewell.service.RememberService;
import com.reborn.back.review.farewell.service.RevealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "farewell", description = "작별 앨범 관련 API")
@RestController
@RequestMapping("/farewell/{farewellId}")
@RequiredArgsConstructor
public class FarewellController {

    private final FarewellService farewellService;
    private final RevealService revealService;
    private final RememberService rememberService;
    private final UserService userService;

    @Operation(summary = "리뷰 카드용 Pet 간략 정보",
            description = "petId 하나만으로 이름·품종·컬러·death·farewellId 를 반환")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "FAREWELL_2001",
                    description  = "Pet 간략 정보 조회 완료되었습니다.")
    })
    @GetMapping("/pet/{petId}")
    public ApiResponse<FarewellSimpleDto> getPetSimple(
            @PathVariable Integer petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        FarewellSimpleDto dto = farewellService.getPetSimpleDto(petId);

        return ApiResponse.onSuccess(SuccessCode.FAREWELL_PET_SIMPLE_SUCCESS, dto);
    }

    @Operation(summary = "작별 앨범 전체 조회", description = "Recognize, Reveal, Remember, Rebirth 전체 리뷰 조회 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "FAREWELL_2002",
                    description = "작별 앨범 조회가 완료되었습니다.")
    })
    @GetMapping("/review")
    public ApiResponse<FarewellResponseDto> getFarewellReview(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        FarewellResponseDto dto = farewellService.getFarewellReview(farewellId);
        return ApiResponse.onSuccess(SuccessCode.FAREWELL_REVIEW_SUCCESS, dto);
    }

    @Operation(summary = "Reveal 리뷰 상세", description = "감정 드러내기 일기 상세 조회 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVEAL_2003", description = "감정 드러내기 상세 조회 완료")
    })
    @GetMapping("/review/{revealId}")
    public ApiResponse<ReviewRevealDto> getReviewReveal(@PathVariable Integer revealId) {
        ReviewRevealDto dto = revealService.getReviewReveal(revealId);
        return ApiResponse.onSuccess(SuccessCode.REVEAL_DETAIL_VIEW_SUCCESS, dto);
    }

    @Operation(summary = "Remember 리뷰 상세", description = "추억 정리하기 상세 조회 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2003", description = "추억 정리하기 상세 조회 완료")
    })
    @GetMapping("/review/{rememberId}")
    public ApiResponse<ReviewRememberDto> getReviewRemember(@PathVariable Integer rememberId) {
        ReviewRememberDto dto = rememberService.getReviewRemember(rememberId);
        return ApiResponse.onSuccess(SuccessCode.REMEMBER_DETAIL_VIEW_SUCCESS, dto);
    }
}
