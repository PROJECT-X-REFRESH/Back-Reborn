package com.reborn.back.comment.service;

import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.converter.CommentConverter;
import com.reborn.back.comment.dto.CommentRequestDto.CommentReqDto;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Integer createComment(Integer boardId, CommentReqDto commentDto, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = CommentConverter.saveComment(commentDto, board, user);

        commentRepository.save(comment);
        boardRepository.incrementCommentCount(boardId);

        return comment.getId();
    }

    @Transactional
    public boolean deleteComment(Integer commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().equals(user)) {
            throw new GeneralException(ErrorCode.COMMENT_DELETE_NOT_ALLOWED);
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

        boardRepository.decrementCommentCount(comment.getBoard().getId());  // ✅ 댓글 수 -1


        return true;
    }

    public List<Comment> findAllByBoardId(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));
        return commentRepository.findAllByBoardAndIsDeletedFalseOrderByIdDesc(board);
    }
}
