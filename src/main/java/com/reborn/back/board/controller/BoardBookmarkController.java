package com.reborn.back.board.controller;

import com.reborn.back.board.service.BoardBookmarkService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "boardBookmark", description = "boardBookmark 관련 api.")
@RestController
@RequestMapping("/board/{boardId}/bookmark")
@RequiredArgsConstructor
public class BoardBookmarkController {

    private final UserService userService;
    private final BoardBookmarkService boardBookmarkService;

    @Operation(summary = "게시물 북마크 토글", description = "게시물의 북마크를 설정 또는 취소하는 api.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOKMARK_200", description = "게시판 북마크 설정/취소 성공")
    })
    @PostMapping("/toggle")
    public ApiResponse<Boolean> toggleBookmark(
            @PathVariable(name = "boardId") Integer boardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        User user = userService.findUserById(customUserDetails.getId());
        boolean isBookmarked = boardBookmarkService.toggleBookmark(boardId, user);

        // 북마크 토글 (true: 설정됨, false: 취소됨)
        if(isBookmarked) {
            return ApiResponse.onSuccess(SuccessCode.BOARD_BOOKMARK_SUCCESS, isBookmarked);
        }
        return ApiResponse.onSuccess(SuccessCode.BOARD_UNBOOKMARK_SUCCESS, isBookmarked);
    }
}