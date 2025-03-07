package com.reborn.back.comment.controller;

import com.reborn.back.comment.converter.CommentConverter;
import com.reborn.back.comment.dto.CommentRequestDto.CommentReqDto;
import com.reborn.back.comment.dto.CommentResponseDto;
import com.reborn.back.comment.service.CommentService;
import com.reborn.back.domain.comment.Comment;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "comment", description = "comment 관련 API")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;
    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "게시물에 댓글을 추가하는 API")
    @PostMapping("/{boardId}/create")
    public ApiResponse<Integer> createComment(
            @PathVariable Integer boardId,
            @RequestBody CommentReqDto commentDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Integer commentId = commentService.createComment(boardId, commentDto, user);
        return ApiResponse.onSuccess(SuccessCode.COMMENT_CREATED, commentId);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API (작성자만 가능)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_2001", description = "댓글 삭제 완료")
    })
    @DeleteMapping("/delete/{commentId}")
    public ApiResponse<Boolean> deleteComment(
            @PathVariable Integer commentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        boolean result = commentService.deleteComment(commentId, user);
        return ApiResponse.onSuccess(SuccessCode.COMMENT_DELETED, result);
    }

    @Operation(summary = "댓글 목록 조회", description = "게시물의 댓글 목록을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_2011", description = "댓글 목록 조회 완료")
    })
    @GetMapping("/{boardId}/list")
    public ApiResponse<CommentResponseDto.CommentListResDto> getCommentList(
            @PathVariable Integer boardId
    ) {
        List<Comment> comments = commentService.findAllByBoardId(boardId);
        return ApiResponse.onSuccess(SuccessCode.COMMENT_LIST_VIEW_SUCCESS, CommentConverter.commentListResDto(comments));
    }

}
