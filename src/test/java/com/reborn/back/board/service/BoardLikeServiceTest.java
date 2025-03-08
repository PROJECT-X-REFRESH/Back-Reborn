package com.reborn.back.board.service;

import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.domain.board.Board;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardLikeServiceTest {

    @Mock
    private BoardLikeRepository boardLikeRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardLikeService boardLikeService;

    private Board mockBoard;
    private User mockUser;

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
    }

    @Test
    @DisplayName("toggleLike - 좋아요 추가")
    void toggleLike_AddLike() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.of(mockBoard));
        given(boardLikeRepository.existsByUserAndBoard(mockUser, mockBoard)).willReturn(false);

        // when
        Board result = boardLikeService.toggleLike(1, mockUser);

        // then
        assertNotNull(result);
        assertEquals(mockBoard.getId(), result.getId());
        verify(boardLikeRepository).insertOrUpdateLike(mockUser.getUid(), mockBoard.getId());
    }

    @Test
    @DisplayName("toggleLike - 좋아요 취소")
    void toggleLike_RemoveLike() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.of(mockBoard));
        given(boardLikeRepository.existsByUserAndBoard(mockUser, mockBoard)).willReturn(true);

        // when
        Board result = boardLikeService.toggleLike(1, mockUser);

        // then
        assertNotNull(result);
        assertEquals(mockBoard.getId(), result.getId());
        verify(boardLikeRepository).deleteByUserAndBoard(mockUser, mockBoard);
    }

    @Test
    @DisplayName("toggleLike - 게시물 찾을 수 없음")
    void toggleLike_BoardNotFound() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class, () -> boardLikeService.toggleLike(1, mockUser));
        assertEquals(ErrorCode.BOARD_NOT_FOUND.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("checkLike - 좋아요 확인 (좋아요 함)")
    void checkLike_Liked() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.of(mockBoard));
        given(boardLikeRepository.existsByUserAndBoard(mockUser, mockBoard)).willReturn(true);

        // when
        boolean isLiked = boardLikeService.checkLike(1, mockUser);

        // then
        assertTrue(isLiked);
    }

    @Test
    @DisplayName("checkLike - 좋아요 확인 (좋아요 안함)")
    void checkLike_NotLiked() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.of(mockBoard));
        given(boardLikeRepository.existsByUserAndBoard(mockUser, mockBoard)).willReturn(false);

        // when
        boolean isLiked = boardLikeService.checkLike(1, mockUser);

        // then
        assertFalse(isLiked);
    }

    @Test
    @DisplayName("getLikeCount - 특정 게시글의 좋아요 수 조회")
    void getLikeCount_Success() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.of(mockBoard));
        given(boardLikeRepository.countByBoard(mockBoard)).willReturn(10);

        // when
        Integer likeCount = boardLikeService.getLikeCount(1);

        // then
        assertEquals(10, likeCount);
    }

    @Test
    @DisplayName("getLikeCount - 게시물 찾을 수 없음")
    void getLikeCount_BoardNotFound() {
        // given
        given(boardRepository.findById(1)).willReturn(java.util.Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class, () -> boardLikeService.getLikeCount(1));
        assertEquals(ErrorCode.BOARD_NOT_FOUND.getCode(), ex.getErrorCode());
    }
}