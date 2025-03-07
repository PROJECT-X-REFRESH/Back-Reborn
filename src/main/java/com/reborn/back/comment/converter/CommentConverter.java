package com.reborn.back.comment.converter;

import com.reborn.back.comment.dto.CommentRequestDto.CommentReqDto;
import com.reborn.back.comment.dto.CommentResponseDto.CommentListResDto;
import com.reborn.back.comment.dto.CommentResponseDto.CommentResDto;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.user.User;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CommentConverter {
    public static Comment saveComment(CommentReqDto comment, Board board, User user) {
        return Comment.builder()
                .user(user)
                .board(board)
                .contents(comment.getContents())
                .isDeleted(false)
                .build();
    }

    public static CommentResDto toCommentResDto(Comment comment) {
        return CommentResDto.builder()
                .id(comment.getId())
                .commentWriter(comment.getUser().getName())
                .writerProfileImage(comment.getUser().getImg())
                .contents(comment.getContents())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentListResDto commentListResDto(List<Comment> comments) {
        List<CommentResDto> commentDtos = comments.stream()
                .map(CommentConverter::toCommentResDto)
                .collect(Collectors.toList());

        return CommentListResDto.builder()
                .commentList(commentDtos)
                .build();
    }
}
