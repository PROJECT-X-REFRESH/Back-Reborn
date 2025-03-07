package com.reborn.back.board.controller;

import com.reborn.back.board.service.BoardLikeService;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "boardLike", description = "bookLike 관련 api.")
@RestController
@RequestMapping("/board/{boardId}/like")
@RequiredArgsConstructor
public class BoardLikeController {

    private final UserService userService;
    private final BoardLikeService boardLikeService;

    @Operation(summary = "게시물 좋아요", description = "게시물에 좋아요를 추가하거나 취소하는 api.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_2001", description = "게시물 좋아요 성공")
    })
    @PostMapping("/toggle")
    public ApiResponse<Boolean> toggleLike(
            @PathVariable(name = "boardId") Integer boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userService.findUserByUserName(userDetails.getUsername());
        boardLikeService.toggleLike(boardId, user);

        return ApiResponse.onSuccess(SuccessCode.BOARD_LIKE_SUCCESS, true);
    }

}
