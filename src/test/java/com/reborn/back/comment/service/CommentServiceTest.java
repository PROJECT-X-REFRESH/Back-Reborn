package com.reborn.back.comment.service;

import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.dto.CommentRequestDto.CommentReqDto;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private CommentService commentService;

    private User mockUser;
    private Board mockBoard;
    private Comment mockComment;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .uid("testUid")
                .name("testUser")
                .email("test@example.com")
                .build();

        mockBoard = Board.builder()
                .id(1)
                .content("Test Board Content")
                .user(mockUser)
                .build();

        mockComment = Comment.builder()
                .id(1)
                .contents("Test Comment")
                .user(mockUser)
                .board(mockBoard)
                .build();
    }

    @Test
    @DisplayName("createComment - 댓글 생성 성공")
    void createComment_Success() {
        // given
        CommentReqDto commentReqDto = CommentReqDto.builder()
                .contents("Test Comment")
                .build();

        given(boardRepository.findById(anyInt())).willReturn(Optional.of(mockBoard));
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);

        // when
        Integer commentId = commentService.createComment(1, commentReqDto, mockUser);

        // then
        assertNotNull(commentId);
        assertEquals(mockComment.getId(), commentId);
        verify(boardRepository).incrementCommentCount(1);
    }

    @Test
    @DisplayName("createComment - 게시글을 찾을 수 없음")
    void createComment_BoardNotFound() {
        // given
        CommentReqDto commentReqDto = CommentReqDto.builder().contents("Test Comment").build();
        given(boardRepository.findById(anyInt())).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> commentService.createComment(1, commentReqDto, mockUser));
        assertEquals(ErrorCode.BOARD_NOT_FOUND.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteComment - 댓글 삭제 성공")
    void deleteComment_Success() {
        // given
        given(commentRepository.findById(anyInt())).willReturn(Optional.of(mockComment));

        // when
        boolean result = commentService.deleteComment(1, mockUser);

        // then
        assertTrue(result);
        assertTrue(mockComment.getIsDeleted());
        verify(commentRepository).save(mockComment);
        verify(boardRepository).decrementCommentCount(mockBoard.getId());
    }

    @Test
    @DisplayName("deleteComment - 삭제 권한 없음")
    void deleteComment_NotAllowed() {
        // given
        User anotherUser = User.builder().uid("anotherUid").name("anotherUser").build();
        given(commentRepository.findById(anyInt())).willReturn(Optional.of(mockComment));

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> commentService.deleteComment(1, anotherUser));
        assertEquals(ErrorCode.COMMENT_DELETE_NOT_ALLOWED.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteComment - 댓글을 찾을 수 없음")
    void deleteComment_CommentNotFound() {
        // given
        given(commentRepository.findById(anyInt())).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> commentService.deleteComment(1, mockUser));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("findAllByBoardId - 게시글의 댓글 목록 조회")
    void findAllByBoardId_Success() {
        // given
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(mockBoard));
        given(commentRepository.findAllByBoardAndIsDeletedFalseOrderByIdDesc(mockBoard))
                .willReturn(Collections.singletonList(mockComment));

        // when
        List<Comment> comments = commentService.findAllByBoardId(1);

        // then
        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getContents());
    }

    @Test
    @DisplayName("findAllByBoardId - 게시글을 찾을 수 없음")
    void findAllByBoardId_BoardNotFound() {
        // given
        given(boardRepository.findById(anyInt())).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> commentService.findAllByBoardId(1));
        assertEquals(ErrorCode.BOARD_NOT_FOUND.getCode(), ex.getErrorCode());
    }
}