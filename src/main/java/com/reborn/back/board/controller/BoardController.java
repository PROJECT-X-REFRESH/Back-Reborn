package com.reborn.back.board.controller;

import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardListResDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardResDto;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.service.BoardService;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "board", description = "board 관련 API")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardLikeRepository boardLikeRepository;

    @Operation(summary = "게시물 생성{POST, SHARE, VOLUNTEER}", description = "게시물을 생성하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2011", description = "게시물 생성이 완료되었습니다.")
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Integer> createBoard(
            @RequestPart(value = "board", required = false) MultipartFile file,
            @RequestPart(value = "data") BoardReqDto boardReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        String dirName = "board/";
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        Board board = boardService.createBoard(boardReqDto, dirName, file, user);

        return ApiResponse.onSuccess(SuccessCode.BOARD_CREATED, board.getId());
    }

    @Operation(summary = "게시물 상세 조회", description = "특정 게시물을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2001", description = "게시물 상세 조회가 완료되었습니다.")
    })
    @GetMapping("/{boardId}")
    public ApiResponse<BoardResDto> getBoard(
            @PathVariable Integer boardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());

        Board board = boardService.findByIdWithSync(boardId);

        boardService.increaseViewCount(boardId, user.getUid());

        boolean liked = boardLikeRepository.existsByUserAndBoard(user, board);

        return ApiResponse.onSuccess(SuccessCode.BOARD_DETAIL_VIEW_SUCCESS, BoardConverter.simpleBoardDto(board, liked));
    }

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제하는 API (작성자만 가능)") // 작성자만 삭제 사능
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2003", description = "게시물 삭제가 완료되었습니다.")
    })
    @DeleteMapping("/{boardId}")
    public ApiResponse<Boolean> deleteBoard(
            @PathVariable Integer boardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        boardService.deleteBoard(boardId, user);
        return ApiResponse.onSuccess(SuccessCode.BOARD_DELETED, true);
    }

    //--------------------------------------------------------------------------------------------------------
    @Operation(summary = "전체 게시물 목록 조회", description = "카테고리별 최신 게시물 목록을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2007", description = "게시물 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "type", description = "조회할 게시글 종류. POST, SHARE, VOLUNTEER, ALL"),
            @Parameter(name = "scrollPosition", description = "가져올 데이터의 시작 위치 (0부터 시작)"),
            @Parameter(name = "fetchSize", description = "한 번에 불러올 게시글 개수")
    })
    @GetMapping("/list")
    public ApiResponse<BoardListResDto> getListBoards(
            @RequestParam(name = "type", defaultValue = "ALL") String type,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "50") int fetchSize,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        User user   = userService.findUserByUserName(customUserDetails.getUsername());
        List<Board> boards = boardService.getBoardList(type, scrollPosition, fetchSize);

        List<Integer> boardIds = boards.stream().map(Board::getId).toList();
        Set<Integer> likedIds = boardIds.isEmpty()
                ? Set.of()
                : new HashSet<>(
                boardLikeRepository.findLikedBoardIds(user.getUid(), boardIds));

        BoardListResDto dto = BoardConverter.boardListResDto(boards, likedIds);

        return ApiResponse.onSuccess(SuccessCode.BOARD_LIST_VIEW_SUCCESS, dto);
    }

    @Operation(summary = "사용자가 좋아요한 게시물 목록 조회", description = "사용자가 좋아요한 게시물을 최신순으로 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2005", description = "좋아요한 게시물 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "scrollPosition", description = "가져올 데이터의 시작 위치 (0부터 시작)"),
            @Parameter(name = "fetchSize", description = "한 번에 불러올 게시글 개수")
    })
    @GetMapping("/list/like")
    public ApiResponse<BoardListResDto> getLikedBoards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "50") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<Board> likedBoards = boardService.getLikedBoardList(user, scrollPosition, fetchSize);

        Set<Integer> likedIds = likedBoards.stream()
                .map(Board::getId)
                .collect(Collectors.toSet());

        return ApiResponse.onSuccess(
                SuccessCode.BOARD_LIKED_LIST_VIEW_SUCCESS,
                BoardConverter.boardListResDto(likedBoards, likedIds));
    }

    @Operation(summary = "인기 게시글 조회", description = "인기 게시글 목록을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2006", description = "인기 게시글 목록 조회가 완료되었습니다.")
    })
    @GetMapping("/popular")
    public ApiResponse<BoardListResDto> getPopularBoards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<Board> boards = boardService.getPopularBoards();

        List<Integer> boardIds = boards.stream().map(Board::getId).toList();
        Set<Integer> likedIds = boardIds.isEmpty()
                ? Set.of()
                : new HashSet<>(
                boardLikeRepository.findLikedBoardIds(user.getUid(), boardIds));

        return ApiResponse.onSuccess(
                SuccessCode.BOARD_POPULAR_LIST_VIEW_SUCCESS,
                BoardConverter.boardListResDto(boards, likedIds));
    }

    @Operation(summary = "게시물 수정", description = "게시물 내용을 수정하는 API") // 작성자만 수정 가능
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2002", description = "게시물 수정이 완료되었습니다.")
    })
    @PutMapping("/{boardId}")
    public ApiResponse<Integer> updateBoard(
            @PathVariable Integer boardId,
            @RequestBody BoardReqDto boardReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Integer updatedId = boardService.updateBoard(boardId, boardReqDto, user);
        return ApiResponse.onSuccess(SuccessCode.BOARD_UPDATED, updatedId);
    }

}
