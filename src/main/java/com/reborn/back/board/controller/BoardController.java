package com.reborn.back.board.controller;

import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.dto.BoardResponseDto.BoardListResDto;
import com.reborn.back.board.repository.BoardBookmarkRepository;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.service.BoardService;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.jwt.CustomUserDetails;
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
import java.util.List;

@Tag(name = "board", description = "board 관련 api.")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardBookmarkRepository boardBookmarkRepository;

    @Operation(summary = "게시판 만들기", description = "게시판을 생성하는 api.")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Integer> createBoard(
            @RequestPart(value = "board", required = false) MultipartFile file,
            @RequestPart("data") BoardReqDto boardReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Board board = boardService.createBoard(boardReqDto, "board/", file, user);
        return ApiResponse.onSuccess(SuccessCode.BOARD_CREATED, board.getId());
    }

    @Operation(summary = "게시물 조회", description = "특정 게시물을 조회하는 api.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2001", description = "게시판 상세 조회가 완료되었습니다.")
    })
    @GetMapping("/{boardId}")
    public ApiResponse<Board> getBoard(@PathVariable Integer boardId) {
        return ApiResponse.onSuccess(SuccessCode.BOARD_DETAIL_VIEW_SUCCESS, boardService.findById(boardId));
    }

    @Operation(summary = "게시판 수정", description = "게시판 내용을 수정하는 api.") // 작성자만 수정 가능
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2002", description = "게시판 수정이 완료되었습니다.")
    })
    @PutMapping("/{boardId}")
    public ApiResponse<Board> updateBoard(
            @PathVariable Integer boardId,
            @RequestBody BoardReqDto boardReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.BOARD_UPDATED, boardService.updateBoard(boardId, boardReqDto, user));
    }


    @Operation(summary = "게시판 삭제", description = "게시판을 삭제하는 api.") // 작성자만 삭제 사능
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2003", description = "게시판 삭제가 완료되었습니다.")
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
    // 1. scrollPosition: 스크롤이 이동한 위치를 나타내는 매개변수
    // 2. fetchSize: 한 번에 가져올 데이터의 개수를 나타내는 매개변수
    @Operation(summary = "전체 게시판 목록 정보 조회 메서드", description = "type, way에 따라 게시판 목록을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2004", description = "게시판 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "type", description = "조회하고 싶은 게시물 타입, EMOTION: 감정, ACTIVITY: 봉사, CHAT: 잡담, ALL: 전체"),
            @Parameter(name = "way", description = "정렬 방식,  like: 좋아요순, time: 최신순"),
            @Parameter(name = "scrollPosition", description = "데이터 가져올 시작 위치. 0부터 시작. scrollPosition * fetchSize가 첫 데이터 주소"),
            @Parameter(name = "fetchSize", description = "가져올 데이터 크기(게시물 개수)")
    })
    @GetMapping("/list")
    public ApiResponse<BoardListResDto> getListBoards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "way") String way,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "1000") int fetchSize
    ){
        BoardType boardType = BoardType.valueOf(type);
        List<Board> boards = boardService.getBoardList(boardType, way, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.BOARD_LIST_VIEW_SUCCESS, BoardConverter.boardListResDto(boards));
    }

    @Operation(summary = "북마크 한 전체 게시판 목록 정보 조회 메서드", description = "북마크 한 게시판 중 type, way에 따라 목록을 조회하는 메서드입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2005", description = "게시판 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "type", description = "조회하고 싶은 게시물 타입, EMOTION: 감정, ACTIVITY: 봉사, CHAT: 잡담, ALL: 전체"),
            @Parameter(name = "way", description = "정렬 방식,  like: 좋아요순, time: 최신순"),
            @Parameter(name = "scrollPosition", description = "데이터 가져올 시작 위치. 0부터 시작. scrollPosition * fetchSize가 첫 데이터 주소"),
            @Parameter(name = "fetchSize", description = "가져올 데이터 크기(게시물 개수)")
    })
    @GetMapping("/list/bookmark")
    public ApiResponse<BoardListResDto> getBookmarkBoards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "way") String way,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "1000") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        BoardType boardType = BoardType.valueOf(type);
        List<Board> bookmarkBoards = boardService.getBookmarkBoardList(user, boardType, way, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.BOARD_LIST_VIEW_SUCCESS, BoardConverter.boardListResDto(bookmarkBoards));
    }
}
