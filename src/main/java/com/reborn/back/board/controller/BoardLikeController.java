package com.reborn.back.board.controller;

import com.reborn.back.board.service.BoardLikeService;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.jwt.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<Integer> toggleLike(
            @PathVariable(name = "boardId") Integer boardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userService.findUserByUserName(userDetails.getUsername());
        Board updatedBoard = boardLikeService.toggleLike(boardId, user);

        return ApiResponse.onSuccess(SuccessCode.BOARD_LIKE_SUCCESS, updatedBoard.getLikeCount());
    }

    @Operation(summary = "게시물 좋아요 개수 조회", description = "게시물 좋아요 개수 조회하는 api.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_2003", description = "게시물 좋아요 개수 조회 성공")
    })
    @GetMapping("/count")
    public ApiResponse<Integer> getLikeCount(@PathVariable(name = "boardId") Integer boardId
    ) {
        Integer likeCount = boardLikeService.getLikeCount(boardId);

        return ApiResponse.onSuccess(SuccessCode.BOARD_LIKE_COUNT_SUCCESS, likeCount);
    }
}
