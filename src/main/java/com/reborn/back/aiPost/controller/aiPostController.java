package com.reborn.back.aiPost.controller;

import com.reborn.back.aiPost.service.AiPostService;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "aipost", description = "aipost 관련 api.")
@RestController
@RequestMapping("/aipost")
@RequiredArgsConstructor
public class aiPostController {
    private final UserService userService;
    private final AiPostService aiPostService;
    @GetMapping("/recent")
    @Operation(summary = "최신 Ai 포스트 3개 조회")
    public ApiResponse<?> getRecentPosts() {
        return ApiResponse.onSuccess(SuccessCode.AIPOST_RECENT_OK, aiPostService.getRecentAiPosts());
    }

    @GetMapping
    @Operation(summary = "Ai 포스트 목록 조회")
    public ApiResponse<?> getAiPosts(
            @RequestParam int scrollPosition,
            @RequestParam int fetchSize) {
        return ApiResponse.onSuccess(SuccessCode.AIPOST_LIST_VIEW_OK, aiPostService.getPostList(scrollPosition, fetchSize));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Ai 포스트 상세 조회")
    public ApiResponse<?> getPostDetail(@PathVariable Integer postId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.AIPOST_DETAIL_OK, aiPostService.getPostDetail(postId, user));
    }

    @PostMapping("/{postId}/bookmark")
    @Operation(summary = "Ai 포스트 북마크 토글")
    public ApiResponse<?> toggleBookmark(@PathVariable Integer postId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.AIPOST_BOOKMARK_TOGGLE_OK, aiPostService.toggleBookmark(postId, user));
    }

    @GetMapping("/bookmark")
    @Operation(summary = "내가 북마크한 Ai 포스트 목록 조회")
    public ApiResponse<?> getMyBookmarks(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam int scrollPosition,
                                         @RequestParam int fetchSize) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.AIPOST_BOOKMARK_LIST_OK, aiPostService.getMyBookmarkedPosts(user, scrollPosition, fetchSize));
    }
}
