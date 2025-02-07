package com.reborn.back.login.service;

import com.reborn.back.board.repository.BoardBookmarkRepository;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.diary.repository.RediaryRepository;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardBookmark;
import com.reborn.back.domain.board.BoardLike;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.diary.Rediary;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.SocialAccount;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.auth.jwt.JwtTokenUtils;
import com.reborn.back.login.auth.jwt.RefreshToken;
import com.reborn.back.login.auth.service.JpaUserDetailsManager;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.mapper.UserConverter;
import com.reborn.back.login.repository.RefreshTokenRepository;
import com.reborn.back.login.repository.UserRepository;
import com.reborn.back.pet.repository.PetRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PetRepository petRepository;
    private final RediaryRepository rediaryRepository;
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardLikeRepository boardLikeRepository;

    private final JpaUserDetailsManager manager;
    private final AmazonS3Manager amazonS3Manager;
    private final JwtTokenUtils jwtTokenUtils;

    // 로그인

    // username으로 User찾기
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND_BY_USERNAME));
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
        return user;
    }

    public Boolean checkMemberByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(UserRequestDto.UserReqDto userReqDto) {
        // 새로운 사용자 생성
        User newUser = userRepository.save(UserConverter.saveUser(userReqDto));

        // 새로운 사용자 정보를 반환하기 전에 저장된 UserDetails를 다시 로드하여 동기화 시도
        manager.loadUserByUsername(userReqDto.getUsername());

        return newUser;
    }

    public void saveNickname(UserRequestDto.UserNicknameReqDto nicknameReqDto, User user) {
        // 입력된 닉네임
        String nickname = nicknameReqDto.getNickname();

        // 중복 검사
        if (userRepository.existsByNickname(nickname)) {
            throw GeneralException.of(ErrorCode.ALREADY_USED_NICKNAME);
        }

        // 중복이 없는 경우 닉네임 저장
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public JwtDto jwtMakeSave(String username) {

        // JWT 생성 - access & refresh
        UserDetails details
                = manager.loadUserByUsername(username);

        JwtDto jwt = jwtTokenUtils.generateToken(details); //2. access, refresh token 생성 및 발급
        log.info("accessToken: {}", jwt.getAccessToken());
        log.info("refreshToken: {} ", jwt.getRefreshToken());

        // 유효기간 초단위 설정 후 db에 refresh token save
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwt.getRefreshToken());
        Long validPeriod
                = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();

        // DB에 저장된 해당 사용자의 리프레시 토큰을 업데이트
        Optional<RefreshToken> existingToken = refreshTokenRepository.findById(username);
        if (existingToken.isPresent()) {
            refreshTokenRepository.deleteById(username);
        }

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(username)
                        .ttl(validPeriod)
                        .refreshToken(jwt.getRefreshToken())
                        .build()
        );

        // JSON 형태로 응답
        return jwt;
    }

    // fcm 토큰 저장
    public void saveFcmToken(User user, String deviceToken) {
        user.setDeviceToken(deviceToken);
        userRepository.save(user);
    }

    // 로그아웃
    public void logout(HttpServletRequest request) {
        // 1. access token 찾아오기
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        // 2. 리프레시 토큰을 username으로 찾아 삭제
        String username = jwtTokenUtils.parseClaims(accessToken).getSubject();
        log.info("access token에서 추출한 username : {}", username);
        if (refreshTokenRepository.existsById(username)) {
            refreshTokenRepository.deleteById(username);
            log.info("DB에서 리프레시 토큰 삭제 완료");
        } else {
            throw GeneralException.of(ErrorCode.WRONG_REFRESH_TOKEN);
        }
    }

    // access, refresh 토큰 재발급
    public JwtDto reissue(HttpServletRequest request) {
        // 1. Request에서 Refresh Token 추출
        String refreshTokenValue = request.getHeader("Authorization").split(" ")[1];

        // 2. DB에서 해당 Refresh Token을 찾음
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new GeneralException(ErrorCode.WRONG_REFRESH_TOKEN));
        log.info("찾은 refresh token : {}", refreshToken);

        // 3. Refresh Token의 유효기간 확인 (생략)

        // 4. Refresh Token을 발급한 사용자 정보 로드
        UserDetails userDetails = manager.loadUserByUsername(refreshToken.getId());
        log.info("refresh token에서 추출한 username : {}", refreshToken.getId());

        // 5. 새로운 Access Token 및 Refresh Token 생성
        JwtDto jwt = jwtTokenUtils.generateToken(userDetails);
        log.info("reissue: refresh token 재발급 완료");

        // 6. Refresh Token 정보 업데이트 및 DB에 저장
        refreshToken.updateRefreshToken(jwt.getRefreshToken());
        Claims refreshTokenClaims = jwtTokenUtils.parseClaims(jwt.getRefreshToken());
        Long validPeriod = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        refreshToken.updateTtl(validPeriod);
        refreshTokenRepository.save(refreshToken);
        log.info("accessToken: {}", jwt.getAccessToken());
        log.info("refreshToken: {} ", jwt.getRefreshToken());

        // 7. DB에 새로운 리프레시 토큰이 정상적으로 저장되었는지 확인
        if (!refreshTokenRepository.existsById(refreshToken.getId())) {
            throw GeneralException.of(ErrorCode.WRONG_REFRESH_TOKEN);
        }

        return jwt;
    }

    // 회원 탈퇴
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 연관된 펫 엔티티들 삭제 -> 15일 컨텐츠 함께 삭제됨
        List<Pet> pets = user.getPetList();
        petRepository.deleteAll(pets);

        // 연관된 리다이어리 엔티티들 삭제
        List<Rediary> rediaries = user.getRediaryList();
        rediaryRepository.deleteAll(rediaries);

        // 연관된 댓글들 삭제
        List<Comment> comments = user.getCommentList();
        commentRepository.deleteAll(comments);

        List<BoardBookmark> boardBookmarks = user.getBoardBookmarkList();
        boardBookmarkRepository.deleteAll(boardBookmarks);

        List<BoardLike> boardLikes = user.getBoardLikeList();
        boardLikeRepository.deleteAll(boardLikes);

        // 연관된 게시물 엔티티들 삭제 -> 관련 댓글, 좋아요, 북마크 테이블도 함께 삭제됨
        List<Board> boards = user.getBoardList();
        boardRepository.deleteAll(boards);

        if (refreshTokenRepository.existsById(username)) {
            refreshTokenRepository.deleteById(username);
            log.info("DB에서 리프레시 토큰 삭제 완료");
        }
        userRepository.delete(user);
        log.info("{} 회원 탈퇴 완료", username);
    }

    @Transactional
    public void createProfileImage(String dirName, MultipartFile file, User user) throws IOException {
        String uploadFileUrl = null;

        if (file != null) {
            String contentType = file.getContentType();
            if (ObjectUtils.isEmpty(contentType)) {
                throw GeneralException.of(ErrorCode.INVALID_FILE_CONTENT_TYPE);
            }

            MediaType mediaType = amazonS3Manager.contentType(Objects.requireNonNull(file.getOriginalFilename()));
            if (mediaType == null || !(mediaType.equals(MediaType.IMAGE_PNG) || mediaType.equals(MediaType.IMAGE_JPEG))) {
                throw GeneralException.of(ErrorCode.MISMATCH_IMAGE_FILE);
            }

            // 이전 프로필 이미지가 존재하는지 확인
            if (!isEmpty(user.getProfileImage())) {
                // 기존 프로필 이미지를 S3에서 삭제
                String previousFilePath = user.getProfileImage();
                amazonS3Manager.deleteFile(previousFilePath); // S3에서 삭제
            }

            java.io.File uploadFile = amazonS3Manager.convert(file)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

            String fileName = dirName + amazonS3Manager.generateFileName(file);
            uploadFileUrl = amazonS3Manager.putS3(uploadFile, fileName);

            user.setProfileImage(uploadFileUrl); // 새로운 사진 url 저장
            userRepository.save(user);
        }
    }

    @Transactional
    public void createBackgroundImage(String dirName, MultipartFile file, User user) throws IOException {
        String uploadFileUrl = null;

        if (file != null) {
            String contentType = file.getContentType();
            if (ObjectUtils.isEmpty(contentType)) {
                throw GeneralException.of(ErrorCode.INVALID_FILE_CONTENT_TYPE);
            }

            MediaType mediaType = amazonS3Manager.contentType(Objects.requireNonNull(file.getOriginalFilename()));
            if (mediaType == null || !(mediaType.equals(MediaType.IMAGE_PNG) || mediaType.equals(MediaType.IMAGE_JPEG))) {
                throw GeneralException.of(ErrorCode.MISMATCH_IMAGE_FILE);
            }

            // 이전 프로필 이미지가 존재하는지 확인
            if (!isEmpty(user.getBackgroundImage())) {
                // 기존 프로필 이미지를 S3에서 삭제
                String previousFilePath = user.getBackgroundImage();
                amazonS3Manager.deleteFile(previousFilePath); // S3에서 삭제
            }

            java.io.File uploadFile = amazonS3Manager.convert(file)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

            String fileName = dirName + amazonS3Manager.generateFileName(file);
            uploadFileUrl = amazonS3Manager.putS3(uploadFile, fileName);

            user.setBackgroundImage(uploadFileUrl); // 새로운 사진 url 저장
            userRepository.save(user);
        }
    }

    @Transactional
    public String showProfileImage(User user) {
        return user.getProfileImage();
    }

    @Transactional
    public String showBackgroundImage(User user) {
        return user.getBackgroundImage();
    }

    @Transactional
    public void setContentPetId(User user, Long id) {
        user.setContentPetId(id);
    }

    @Transactional
    public void resetContentPetId(User user) {
        user.setContentPetId(null);
    }

    @Transactional
    public void unifyAccountsByPhoneNum(String phoneNum, User currentUser) {
        // phoneNum을 가진 다른 User가 있는지?
        User existingUser = userRepository.findByPhoneNum(phoneNum).orElse(null);
        if (existingUser == null) { // 없으면 -> currentUser의 phoneNum 설정
            currentUser.setPhoneNum(phoneNum);
            userRepository.save(currentUser);
        } else { // 있으면 -> 두 계정을 하나로 통합
            if (!existingUser.getId().equals(currentUser.getId())) {
                unifyAccounts(existingUser, currentUser);
            }
        }
    }

    @Transactional
    public void unifyAccounts(User toUser, User fromUser) {
        log.info("계정 통합: {} <- {}", toUser.getId(), fromUser.getId());
        // 1) fromUser가 가진 소셜 계정, 게시글, 댓글 등 전부 toUser로 소유자 변경
        for (SocialAccount sa : fromUser.getSocialAccounts()) {
            sa.setUser(toUser);
            toUser.getSocialAccounts().add(sa);
        }
        // 2) fromUser 삭제
        userRepository.delete(fromUser);
        userRepository.save(toUser);
    }


    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByPhoneNum(String phoneNum) {
        return (User) userRepository.findByPhoneNum(phoneNum).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
