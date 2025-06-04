package com.reborn.back.board.service;

import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final RedisUtil redisUtil;


    // 조회수 증가 로직 (조회 중복 방지 + 인기글 반영)
    @Transactional
    public void increaseViewCount(Integer boardId, String userId) {
        String cacheKey = "board_view:" + boardId;

        // Redis에서 해당 게시글의 조회 목록 확인
        Set<String> userViews = redisUtil.getSetData(cacheKey);

        if (userViews != null && userViews.contains(userId)) {
            log.info("[CACHE] 사용자 {}는 오늘 이미 게시글 {}을 조회함", userId, boardId);
            return;
        }

        // Redis에 조회 기록 추가 (SET에 userId 저장)
        redisUtil.addToSet(cacheKey, userId);
        redisUtil.expireKey(cacheKey, 86400); // TTL 24시간 설정

        // DB의 조회수 증가
        boardRepository.incrementViewCount(boardId);
    }

    // 인기글 갱신 로직
    public List<Board> getPopularBoards(String type) {
        Set<String> keys = redisUtil.getKeys("board_view:*");
        if (keys == null || keys.isEmpty()) {
            log.info("[CACHE] 인기글 데이터 없음");
            return List.of();
        }

        Map<Integer, Integer> viewCnt = new HashMap<>();
        for (String key : keys) {
            Integer boardId = Integer.parseInt(key.split(":")[1]);
            int cnt = redisUtil.getSetData(key).size(); // 유저 수
            viewCnt.put(boardId, cnt);
        }

        List<Integer> topIds = viewCnt.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        BoardType filter = "ALL".equalsIgnoreCase(type) ? null
                : BoardType.valueOf(type.toUpperCase());

        return topIds.stream()
                .map(id -> boardRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .filter(b -> filter == null || b.getCategory() == filter)   // 🔄
                .toList();
    }

    //--------------------------------------------------------------------------------------------------------

    // 특정 게시물 조회
    @Transactional
    public Board findById(Integer bId) {
        return boardRepository.findById(bId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.BOARD_NOT_FOUND));
    }

    // 게시물 생성
    @Transactional
    public Board createBoard(BoardReqDto boardReqDto, String dirName, MultipartFile file, User user) throws IOException {
        Board board = BoardConverter.saveBoard(boardReqDto, user); // 게시물 내용 저장

        // 파일이 있을 경우에만 업로드 진행
        if (file != null && !file.isEmpty()) {
            board.setAttachImg(uploadFileToS3(dirName, file));
        }

        return boardRepository.save(board);
    }

    // S3 파일 업로드
    private String uploadFileToS3(String dirName, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String contentType = file.getContentType();
        if (ObjectUtils.isEmpty(contentType)) {
            throw GeneralException.of(ErrorCode.INVALID_FILE_CONTENT_TYPE);
        }

        java.io.File uploadFile = amazonS3Manager.convert(file)
                .orElseThrow(() -> new IllegalArgumentException("파일 변환 실패"));

        String fileName = dirName + amazonS3Manager.generateFileName(file);
        return amazonS3Manager.putS3(uploadFile, fileName);
    }

    // 게시물 업데이트
    @Transactional
    public Integer updateBoard(Integer bId, BoardReqDto boardReqDto, User user) {
        Board board = findById(bId);

        if (!Objects.equals(board.getUser().getUid(), user.getUid())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        board.setCategory(boardReqDto.getCategory());
        board.setContent(boardReqDto.getContent());

        boardRepository.save(board);

        return board.getId();
    }

    // 게시물 삭제
    @Transactional
    public void deleteBoard(Integer bId, User user) {
        Board board = findById(bId);

        if (!Objects.equals(board.getUser().getUid(), user.getUid())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        String cacheKey = "board_view:" + bId;
        redisUtil.deleteData(cacheKey);

        boardLikeRepository.deleteAll(board.getBoardLikeList());
        boardRepository.delete(board);
    }

    // 특정 카테고리별 게시물 조회 (최신순)
    @Transactional(readOnly = true)
    public List<Board> getBoardList(String type, int scrollPosition, int fetchSize) {

        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);

        // 1) ALL → 카테고리 무시
        if ("ALL".equalsIgnoreCase(type)) {
            Slice<Board> slice = boardRepository.findAllByOrderByCreatedAtDesc(pageRequest);
            return slice.getContent();
        }

        // 2) 특정 카테고리
        BoardType boardType = BoardType.valueOf(type.toUpperCase());
        Slice<Board> slice = boardRepository.findByCategoryOrderByCreatedAtDesc(boardType, pageRequest);
        return slice.getContent();
    }

    // 사용자가 좋아요한 게시글 리스트 조회
    @Transactional
    public List<Board> getLikedBoardList(User user, String type, int scrollPosition, int fetchSize) {
        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);
        BoardType boardType = "ALL".equalsIgnoreCase(type) ? null : BoardType.valueOf(type.toUpperCase());

        Slice<Board> boardSlice;
        if (boardType == null) {
            boardSlice = boardRepository.findByBoardLikeList_User_UidOrderByCreatedAtDesc(user.getUid(), pageRequest);
        } else {
            boardSlice = boardRepository.findByBoardLikeList_User_UidAndCategoryOrderByCreatedAtDesc(user.getUid(), boardType, pageRequest);
        }

        return boardSlice.getContent();
    }

    // DB에 저장된 commentCount와 실제 Comment 개수를 비교(필요 시 업데이트)
    @Transactional
    public Board findByIdWithSync(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorCode.BOARD_NOT_FOUND));

        Integer actualCommentCount = commentRepository.countByBoard(board);

        if (board.getCommentCount() != actualCommentCount) {
            board.setCommentCount(actualCommentCount);
            boardRepository.save(board);
        }

        return board;
    }
}
