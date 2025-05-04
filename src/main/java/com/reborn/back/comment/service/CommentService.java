package com.reborn.back.comment.service;

import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.converter.CommentConverter;
import com.reborn.back.comment.dto.CommentRequestDto.CommentReqDto;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.user.User;
import com.reborn.back.fcm.dto.FcmRequestDto;
import com.reborn.back.fcm.service.FcmService;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final FcmService fcmService;

    @Transactional
    public Integer createComment(Integer boardId, CommentReqDto commentDto, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = CommentConverter.saveComment(commentDto, board, user);

        commentRepository.save(comment);
        boardRepository.incrementCommentCount(boardId); // 댓글 수 +1

        sendCommentPush(board, user);

        return comment.getId();
    }

    @Transactional
    public boolean deleteComment(Integer commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().equals(user)) {
            throw new GeneralException(ErrorCode.COMMENT_DELETE_NOT_ALLOWED);
        }


        commentRepository.delete(comment);

        boardRepository.decrementCommentCount(comment.getBoard().getId());  // 댓글 수 -1

        return true;
    }

    private void sendCommentPush(Board board, User writer) {
        User postAuthor = board.getUser();
        // 자기 글에 자기가 쓴 댓글이면 알림 건너뜀
        if (postAuthor.getUid().equals(writer.getUid())) {
            return;
        }

        String token = postAuthor.getDeviceToken();
        if (token == null || token.isBlank()) {
            log.warn("게시글 작성자({}) deviceToken 없음, 푸시 건너뜀", postAuthor.getUid());
            return;
        }

        String title = "댓글이 달렸어요";
        String body  = writer.getName() + "님이 댓글을 남겼습니다.";

        try {
            fcmService.sendMessage(FcmRequestDto.builder()
                    .token(token)
                    .title(title)
                    .body(body)
                    .build());
        } catch (Exception e) {
            log.error("FCM 푸시 전송 실패: {}", e.getMessage(), e);
        }
    }
}
