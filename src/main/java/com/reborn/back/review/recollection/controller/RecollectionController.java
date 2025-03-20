package com.reborn.back.review.recollection.controller;

import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.recollection.dto.RecollectionDto;
import com.reborn.back.review.recollection.service.RecollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2004", description = "게시물 목록 조회가 완료되었습니다.")
    })
    @PostMapping("/week/{petId}")
    public ApiResponse<List<RecollectionDto>> getWeeksRecollection(
            @PathVariable Integer petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<RecollectionDto> recollections = recollectionService.getThisWeekList(user, petId);
        return ApiResponse.onSuccess(SuccessCode.RECOLLECTION_WEEK_VIEW_SUCCESS, recollections);
    }

}
