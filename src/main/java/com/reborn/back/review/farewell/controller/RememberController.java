package com.reborn.back.review.farewell.controller;

import com.reborn.back.domain.review.farewell.Remember;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.farewell.dto.RememberRequestDto.RememberReqDto;
import com.reborn.back.review.farewell.dto.RememberResponseDto.DetailRememberDto;
import com.reborn.back.review.farewell.service.FarewellService;
import com.reborn.back.review.farewell.service.RememberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "remember", description = "remember 관련 API")
@RestController
@RequestMapping("/farewell/{farewellId}/remember")
@RequiredArgsConstructor
public class RememberController {

    private final RememberService rememberService;
    private final UserService userService;
    private final FarewellService farewellService;

    //반려동물과 추억 정리하기
    @Operation(summary = "Remember 생성", description = "fstep에 따라 Remember을 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2011", description = "반려동물과 추억 정리하기 생성이 완료되었습니다.")
    })
    @PostMapping("/create")
    public ApiResponse<Integer> createRemember(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        Remember remember = rememberService.createRemember(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REMEMBER_CREATED, remember.getId());
    }

    @Operation(summary = "컨텐츠 상태 변경", description = "Remember 컨텐츠(feed, snack, walk)의 상태를 변경하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2003", description = "컨텐츠가 완료되었습니다.")
    })
    @PatchMapping("/{activityType}")
    public ApiResponse<String> updateRememberActivity(
            @PathVariable Integer farewellId,
            @PathVariable String activityType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        rememberService.updateRememberActivity(farewellId, activityType);
        return ApiResponse.onSuccess(SuccessCode.REMEMBER_ACTIVITY_UPDATED, activityType);
    }

    @Operation(summary = "그림 일기 생성", description = "그림 일기를 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2011", description = "그림 일기 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Boolean> createRemember(
            @PathVariable Integer farewellId,
            @RequestPart(value = "remember", required = false) MultipartFile file,
            @RequestPart(value = "data") RememberReqDto rememberReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        String dirName = "remember/";
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        rememberService.writeRemember(farewellId, rememberReqDto, dirName, file);
        return ApiResponse.onSuccess(SuccessCode.REMEMBER_WRITE_COMPLETED, true);
    }

    @Operation(summary = "물품 정리", description = "물품을 정리하는 API입니다(SNACK, TOY, BATH, LIVING)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2005", description = "정리 품목이 정상적으로 등록되었습니다.")
    })
    @PatchMapping("/remember/clean/{cleanType}")
    public ApiResponse<String> cleanThing(
            @PathVariable Integer farewellId,
            @PathVariable String cleanType
    ) {
        rememberService.cleanThing(farewellId, cleanType);
        return ApiResponse.onSuccess(SuccessCode.THING_CLEANED_SUCCESS, cleanType);
    }

    @Operation(summary = "Remember 진행 상황", description = "진행중인 Remember의 상태를 전달하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REMEMBER_2002", description = "반려동물과 추억 정리하기 조회가 완료되었습니다.")
    })
    @GetMapping("/view")
    public ApiResponse<DetailRememberDto> getDetailRemember(
            @PathVariable Integer farewellId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        DetailRememberDto detail = rememberService.getDetailRemember(farewellId);
        return ApiResponse.onSuccess(SuccessCode.REMEMBER_DETAIL_VIEW_SUCCESS, detail);
    }
}
