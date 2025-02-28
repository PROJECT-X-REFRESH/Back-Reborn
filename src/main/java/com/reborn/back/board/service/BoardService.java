package com.reborn.back.board.service;

import com.reborn.back.board.dto.BoardRequestDto.BoardReqDto;
import com.reborn.back.board.repository.BoardBookmarkRepository;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.board.converter.BoardConverter;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BoardType;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final AmazonS3Manager amazonS3Manager;

    // 전체 게시물
    @Transactional
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    // 특정 게시물 조회
    @Transactional
    public Board findById(Integer bId){
        return boardRepository.findById(bId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.BOARD_NOT_FOUND));
    }

    // 사용자가 작성한 게시물
    @Transactional
    public List<Board> findByUser(User user){
        return user.getBoardList().stream().toList();
    }

    // 게시물 생성
    @Transactional
    public Board createBoard(BoardReqDto boardReqDto, String dirName, MultipartFile file, User user) throws IOException {
        Board board = BoardConverter.saveBoard(boardReqDto, user); // 게시판 내용 저장

        board.setAttachImg(uploadFileToS3(dirName, file));

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
    public Board updateBoard(Integer bId, BoardReqDto boardReqDto, User user) {
        Board board = findById(bId);

        if (!Objects.equals(board.getUser().getUid(), user.getUid())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        board.setCategory(boardReqDto.getCategory());
        board.setContent(boardReqDto.getContent());
        return boardRepository.save(board);
    }

    // 게시물 삭제
    @Transactional
    public void deleteBoard(Integer bId, User user) {
        Board board = findById(bId);

        if (!Objects.equals(board.getUser().getUid(), user.getUid())) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        boardBookmarkRepository.deleteAll(board.getBoardBookmarkList());
        boardLikeRepository.deleteAll(board.getBoardLikeList());

        boardRepository.delete(board);
    }

    // 특정 카테고리별 게시물 조회 (최신순 or 좋아요순)
    @Transactional
    public List<Board> getBoardList(BoardType boardType, String way, int scrollPosition, int fetchSize) {
        Slice<Board> boardSlice;
        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);
        if (way.equals("like")) {
            // 좋아요 순
            boardSlice = boardRepository.findByCategoryOrderByLikeCountDesc(boardType, pageRequest);
        } else {
            // 시간 순
            boardSlice = boardRepository.findByCategoryOrderByCreatedAtDesc(boardType, pageRequest);
        }
        return boardSlice.getContent();
    }

    // 북마크한 게시글 리스트 조회
    public List<Board> getBookmarkBoardList(User user, BoardType boardType, String way, int scrollPosition, int fetchSize) {
        Slice<Board> boardSlice;
        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);

        boardSlice = boardRepository.findByBookmarkList_User_UidOrderByCreatedAtDesc(user.getUid(), boardType, pageRequest);

        return boardSlice.getContent();
    }
}
