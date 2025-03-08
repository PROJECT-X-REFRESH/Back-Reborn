package com.reborn.back.board.service;

import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardLikeRepository boardLikeRepository;

    @Mock
    private AmazonS3Manager amazonS3Manager;

    @Mock
    private RedisUtil redisUtil;

    private User mockUser;
    private Board mockBoard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder().uid("user123").name("testUser").build();
        mockBoard = Board.builder().id(1).content("test content").user(mockUser).build();
    }

    @Test
    @DisplayName("increaseViewCount - 중복 조회 방지")
    void increaseViewCount_NoDuplicateView() {
        // given
        Integer boardId = 1;
        String userId = "user123";

        given(redisUtil.getSetData("board_view:1")).willReturn(new HashSet<>(Collections.singletonList(userId)));

        // when
        boardService.increaseViewCount(boardId, userId);

        // then
        verify(boardRepository, never()).incrementViewCount(boardId);
    }

    @Test
    @DisplayName("getPopularBoards - 인기 게시물 조회")
    void getPopularBoards_Success() {
        // given
        Set<String> keys = Set.of("board_view:1", "board_view:2");
        given(redisUtil.getKeys("board_view:*")).willReturn(keys);
        given(redisUtil.getSetData("board_view:1")).willReturn(Set.of("user1", "user2"));
        given(redisUtil.getSetData("board_view:2")).willReturn(Set.of("user3"));
        given(boardRepository.findById(1)).willReturn(Optional.of(mockBoard));

        // when
        List<Board> popularBoards = boardService.getPopularBoards();

        // then
        assertEquals(1, popularBoards.size());
        assertEquals("test content", popularBoards.get(0).getContent());
    }

    @Test
    @DisplayName("findById - 존재하지 않는 게시물 조회 시 예외 발생")
    void findById_NotFound() {
        // given
        given(boardRepository.findById(1)).willReturn(Optional.empty());

        // when & then
        assertThrows(GeneralException.class, () -> boardService.findById(1));
    }

    @Test
    @DisplayName("createBoard - 게시물 생성 (이미지 포함)")
    void createBoard_WithImage() throws IOException {
        // given
        BoardReqDto boardReqDto = BoardReqDto.builder().content("New Content").build();
        MultipartFile file = mock(MultipartFile.class);

        // Mock 설정
        given(file.isEmpty()).willReturn(false);
        given(file.getContentType()).willReturn("image/jpeg"); // Content Type 설정

        java.io.File mockFile = mock(java.io.File.class);
        given(amazonS3Manager.convert(file)).willReturn(Optional.of(mockFile));
        given(amazonS3Manager.putS3(mockFile, "dir/")).willReturn("s3-image-url");

        Board mockBoard = Board.builder()
                .content("New Content")
                .attachImg("s3-image-url")
                .build();

        // boardRepository.save 메서드에 대한 Mock 설정 추가
        given(boardRepository.save(any(Board.class))).willReturn(mockBoard);

        // when
        Board createdBoard = boardService.createBoard(boardReqDto, "dir/", file, mockUser);

        // then
        assertNotNull(createdBoard);
        assertEquals("s3-image-url", createdBoard.getAttachImg());
    }

    @Test
    @DisplayName("updateBoard - 권한 없는 사용자의 업데이트 시도 시 예외 발생")
    void updateBoard_Unauthorized() {
        // given
        User otherUser = User.builder().uid("otherUser").build();
        Board board = Board.builder().id(1).content("Original").user(otherUser).build();
        BoardReqDto boardReqDto = BoardReqDto.builder().content("Updated Content").build();
        given(boardRepository.findById(1)).willReturn(Optional.of(board));

        // when & then
        assertThrows(GeneralException.class, () -> boardService.updateBoard(1, boardReqDto, mockUser));
    }

    @Test
    @DisplayName("deleteBoard - 게시물 삭제 및 Redis 캐시 삭제")
    void deleteBoard_Success() {
        // given
        given(boardRepository.findById(1)).willReturn(Optional.of(mockBoard));

        // when
        boardService.deleteBoard(1, mockUser);

        // then
        verify(boardRepository).delete(mockBoard);
        verify(redisUtil).deleteData("board_view:1");
    }

    @Test
    @DisplayName("getBoardList - 특정 카테고리별 게시물 조회")
    void getBoardList_Success() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        BoardType boardType = BoardType.SHARE;
        List<Board> boards = List.of(mockBoard);
        Slice<Board> boardSlice = new SliceImpl<>(boards, pageRequest, true);

        given(boardRepository.findByCategoryOrderByCreatedAtDesc(boardType, pageRequest)).willReturn(boardSlice);

        // when
        List<Board> result = boardService.getBoardList(boardType, 0, 10);

        // then
        assertEquals(1, result.size());
        assertEquals(mockBoard, result.get(0));
    }
}
