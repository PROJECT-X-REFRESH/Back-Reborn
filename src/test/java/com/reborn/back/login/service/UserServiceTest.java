package com.reborn.back.login.service;

import com.reborn.back.aiPost.service.AiPostService;
import com.reborn.back.board.repository.BoardLikeRepository;
import com.reborn.back.board.repository.BoardRepository;
import com.reborn.back.comment.repository.CommentRepository;
import com.reborn.back.domain.aiPost.AiPost;
import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardLike;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.auth.jwt.JwtTokenUtils;
import com.reborn.back.login.auth.service.JpaUserDetailsManager;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.repository.UserRepository;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.service.RecordService;
import com.reborn.back.review.recollection.service.RemindService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private BoardLikeRepository boardLikeRepository;
    @Mock
    private RedisUtil redisUtil;
    @Mock
    private JpaUserDetailsManager manager;
    @Mock
    private AmazonS3Manager amazonS3Manager;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @Mock
    private RecordService recordService;
    @Mock
    private RemindService remindService;
    @Mock
    private AiPostService aiPostService;
    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .uid("testUid")
                .name("testUser")
                .email("test@example.com")
                .img(null)
                .build();
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("testUser");
    }


    @Test
    @DisplayName("findUserByUserName - 사용자 정상 조회")
    void findUserByUserName_Success() {
        // given
        given(userRepository.findByName("testUser")).willReturn(Optional.of(mockUser));

        // when
        User result = userService.findUserByUserName("testUser");

        // then
        assertNotNull(result);
        assertEquals("testUser", result.getName());
        then(userRepository).should(times(1)).findByName("testUser");
    }

    @Test
    @DisplayName("findUserByUserName - 사용자 미존재 시 예외 발생")
    void findUserByUserName_NotFound() {
        // given
        given(userRepository.findByName("unknownUser")).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> userService.findUserByUserName("unknownUser")
        );

        assertEquals(ErrorCode.USER_NOT_FOUND_BY_USERNAME.getCode(), ex.getErrorCode());
        then(userRepository).should(times(1)).findByName("unknownUser");
    }

    @Test
    @DisplayName("findUserById - 사용자 정상 조회")
    void findUserById() {
        // given
        given(userRepository.findByUid("testUid")).willReturn(Optional.of(mockUser));

        // when
        User result = userService.findUserById("testUid");

        // then
        assertNotNull(result);
        assertEquals("testUser", result.getName());
        then(userRepository).should().findByUid("testUid");
    }

    @Test
    @DisplayName("findUserById - 존재하지 않는 ID 조회 시 예외")
    void findUserById_NotFound() {
        // given
        given(userRepository.findByUid("invalidUid")).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> userService.findUserById("invalidUid")
        );
        assertEquals(ErrorCode.USER_NOT_FOUND_BY_USERNAME.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("findByEmail - 정상 조회")
    void findByEmail() {
        // given
        given(userRepository.findByEmail("test@example.com")).willReturn(Optional.of(mockUser));

        // when
        User foundUser = userService.findByEmail("test@example.com");

        // then
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getName());
    }

    @Test
    @DisplayName("findByEmail - 존재하지 않는 이메일 시 예외")
    void findByEmail_NotFound() {
        // given
        given(userRepository.findByEmail("nope@example.com")).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> userService.findByEmail("nope@example.com")
        );
        assertEquals(ErrorCode.USER_NOT_FOUND_BY_EMAIL.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("checkMemberByEmail - 이미 등록된 사용자인 경우 true 반환")
    void checkMemberByEmail_Exists() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(true);

        // when
        boolean result = userService.checkMemberByEmail("test@example.com");

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("checkMemberByEmail - 등록되지 않은 사용자인 경우 false 반환")
    void checkMemberByEmail_NotExists() {
        // given
        given(userRepository.existsByEmail("nope@example.com")).willReturn(false);

        // when
        boolean result = userService.checkMemberByEmail("nope@example.com");

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("createUser - 새 사용자 생성")
    void createUser() {
        // given
        UserRequestDto userRequest = UserRequestDto.builder()
                .username("newUser")
                .email("new@example.com")
                .build();
        // Mock userRepository
        User newUser = User.builder()
                .uid("someUid")
                .name(userRequest.getUsername())
                .email(userRequest.getEmail())
                .build();
        given(userRepository.save(any(User.class))).willReturn(newUser);

        // when
        User createdUser = userService.createUser(userRequest);

        // then
        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getName());
        then(manager).should().loadUserByUsername("newUser");
    }

    @Test
    @DisplayName("jwtMakeSave - JWT 생성 및 Redis에 Refresh Token 저장")
    void jwtMakeSave() {
        // given
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        JwtDto jwtDto = JwtDto.builder()
                .accessToken("mockAccess")
                .refreshToken("mockRefresh")
                .build();
        Claims claims = mock(Claims.class);

        given(manager.loadUserByUsername(username)).willReturn(userDetails);
        given(jwtTokenUtils.generateToken(userDetails)).willReturn(jwtDto);
        // mock valid period
        given(jwtTokenUtils.parseClaims(jwtDto.getRefreshToken())).willReturn(claims);
        given(claims.getExpiration()).willReturn(new Date(System.currentTimeMillis() + 10000));
        given(claims.getIssuedAt()).willReturn(new Date(System.currentTimeMillis()));

        // when
        JwtDto result = userService.jwtMakeSave(username);

        // then
        assertNotNull(result);
        assertEquals("mockAccess", result.getAccessToken());
        then(redisUtil).should().setDataExpire(
                eq("username" + username),
                eq("mockRefresh"),
                anyLong()
        );
    }

    @Test
    @DisplayName("saveFcmToken - FCM 토큰 저장")
    void saveFcmToken() {
        // given
        User user = mockUser;
        String deviceToken = "fcmToken";

        // when
        userService.saveFcmToken(user, deviceToken);

        // then
        assertEquals(deviceToken, user.getDeviceToken());
        then(userRepository).should().save(user);
    }

    @Test
    @DisplayName("logout - 정상 로그아웃 시 Redis의 리프레시 토큰 삭제")
    void logout() {
        // given
        String username = "testUser";
        String accessToken = "dummyAccessToken";
        String refreshToken = "dummyRefreshToken";

        // Mock Claims 객체 생성
        Claims claims = mock(Claims.class);
        given(claims.getSubject()).willReturn(username);

        // Mock parseClaims 메서드
        given(jwtTokenUtils.parseClaims(anyString())).willReturn(claims);

        // Mock Redis 데이터
        given(redisUtil.getData("username" + username)).willReturn(refreshToken);

        // Mock HttpServletRequest
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);

        // when
        userService.logout(request);

        // then
        then(redisUtil).should().deleteData("username" + username);
    }

    @Test
    @DisplayName("logout - 잘못된 토큰 시 예외 발생")
    void logoutWrongToken() {
        // given
        String username = "testUser";
        String accessToken = "dummyAccessToken";

        // Mock Claims 객체 생성
        Claims claims = mock(Claims.class);
        given(claims.getSubject()).willReturn(username);

        // Mock JwtTokenUtils의 parseClaims 메서드
        given(jwtTokenUtils.parseClaims(anyString())).willReturn(claims);

        // Redis에서 null을 반환하도록 설정
        given(redisUtil.getData("username" + username)).willReturn(null);

        // Mock HttpServletRequest
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + accessToken);

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> userService.logout(request)
        );

        assertEquals(ErrorCode.WRONG_REFRESH_TOKEN.getCode(), ex.getErrorCode());
    }
    @Test
    @DisplayName("reissue - 정상적으로 토큰 재발급 (성공 시나리오)")
    void reissue_Success() {
        // given
        String username = "testUser";
        String oldRefreshToken = "oldRefreshToken";
        String newRefreshToken = "newRefreshToken";
        Claims claims = mock(Claims.class);
        UserDetails userDetails = mock(UserDetails.class);

        // Redis에 "username + username" 키로 oldRefreshToken이 저장되어 있다고 가정
        given(redisUtil.getData("username" + username))
                .willReturn(oldRefreshToken)
                .willReturn(newRefreshToken);

        // request에는 "Bearer oldRefreshToken"이 들어온다고 설정
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + oldRefreshToken);

        // parseClaims가 oldRefreshToken을 파싱했을 때 username="testUser"를 리턴하도록 설정
        given(jwtTokenUtils.parseClaims(oldRefreshToken)).willReturn(claims);
        given(claims.getSubject()).willReturn(username);

        // username으로 사용자 정보 조회 Mock
        given(manager.loadUserByUsername(username)).willReturn(userDetails);

        // generateToken()이 새롭게 발급한 토큰
        JwtDto newTokens = JwtDto.builder()
                .accessToken("newAccess")
                .refreshToken(newRefreshToken)
                .build();
        given(jwtTokenUtils.generateToken(userDetails)).willReturn(newTokens);

        // 새로 발급된 Refresh Token을 parse할 때 필요한 Claims Mock
        Claims newClaims = mock(Claims.class);
        given(jwtTokenUtils.parseClaims(newRefreshToken)).willReturn(newClaims);
        given(newClaims.getExpiration()).willReturn(new Date(System.currentTimeMillis() + 10_000));
        given(newClaims.getIssuedAt()).willReturn(new Date());

        // when
        JwtDto result = userService.reissue(request);

        // then
        // 1) 예외 발생 없이 정상적으로 재발급 성공
        assertNotNull(result);
        assertEquals("newAccess", result.getAccessToken());
        assertEquals("newRefreshToken", result.getRefreshToken());

        // 2) Redis에 새 토큰 저장했는지 검증
        then(redisUtil).should().deleteData("username" + username);
        then(redisUtil).should().setDataExpire(eq("username" + username), eq("newRefreshToken"), anyLong());
    }

    @Test
    @DisplayName("reissue - Redis 저장된 토큰과 요청 토큰 불일치 시 예외 발생 (실패 시나리오)")
    void reissue_Fail_DifferentToken() {
        // given
        String username = "testUser";
        String refreshTokenInRedis = "redisStoredRefresh";
        String requestRefreshToken = "incomingOldRefreshToken"; // 서로 다른 값

        Claims claims = mock(Claims.class);

        // Redis에 "redisStoredRefresh"가 저장됨
        given(redisUtil.getData("username" + username)).willReturn(refreshTokenInRedis);

        // Request 헤더에는 "Bearer incomingOldRefreshToken"
        jakarta.servlet.http.HttpServletRequest request = mock(jakarta.servlet.http.HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn("Bearer " + requestRefreshToken);

        // parseClaims가 requestRefreshToken을 파싱해서 username="testUser" 리턴
        given(jwtTokenUtils.parseClaims(requestRefreshToken)).willReturn(claims);
        given(claims.getSubject()).willReturn(username);

        // when & then
        GeneralException ex = assertThrows(
                GeneralException.class,
                () -> userService.reissue(request)
        );
        // "WRONG_REFRESH_TOKEN" 에러 코드인지 혹은 ReasonDTO 등이 맞는지 검증
        assertEquals(ErrorCode.WRONG_REFRESH_TOKEN.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteUser - 사용자 및 연관된 엔티티 제거")
    void deleteUser() {
        // given
        List<Pet> pets = Collections.singletonList(mock(Pet.class));
        List<Comment> comments = Collections.singletonList(mock(Comment.class));
        List<BoardLike> boardLikes = Collections.singletonList(mock(BoardLike.class));
        List<Board> boards = Collections.singletonList(mock(Board.class));
        mockUser.setPetList(pets);
        mockUser.setCommentList(comments);
        mockUser.setBoardLikeList(boardLikes);
        mockUser.setBoardList(boards);

        String username = "testUser";
        given(userRepository.findByName(username)).willReturn(Optional.of(mockUser));
        given(redisUtil.getData("username" + username)).willReturn("someRefreshToken");

        // when
        userService.deleteUser(username);

        // then
        InOrder inOrder = inOrder(petRepository, commentRepository, boardLikeRepository, boardRepository, userRepository, redisUtil);
        inOrder.verify(petRepository).deleteAll(pets);
        inOrder.verify(commentRepository).deleteAll(comments);
        inOrder.verify(boardLikeRepository).deleteAll(boardLikes);
        inOrder.verify(boardRepository).deleteAll(boards);
        inOrder.verify(redisUtil).deleteData("username" + username);
        inOrder.verify(userRepository).delete(mockUser);
    }

    @Test
    @DisplayName("createProfileImage - 정상 처리")
    void createProfileImage() throws IOException {
        // given
        MultipartFile file = mock(MultipartFile.class);
        given(file.getContentType()).willReturn("image/jpeg");
        given(file.getOriginalFilename()).willReturn("profile.jpg");
        given(amazonS3Manager.contentType("profile.jpg")).willReturn(MediaType.IMAGE_JPEG);
        given(amazonS3Manager.convert(file)).willReturn(Optional.of(new java.io.File("testFile")));
        given(amazonS3Manager.generateFileName(file)).willReturn("generatedName");
        given(amazonS3Manager.putS3(any(java.io.File.class), eq("dirNamegeneratedName"))).willReturn("https://s3/dirNamegeneratedName");

        // when
        userService.createProfileImage("dirName", file, mockUser);

        // then
        assertNotNull(mockUser.getImg());
        assertTrue(mockUser.getImg().contains("generatedName"));
        then(userRepository).should().save(mockUser);
    }

    @Test
    @DisplayName("showProfileImage - 정상적으로 이미지 URL 반환")
    void showProfileImage() {
        // given
        mockUser.setImg("https://s3/oldImg");
        // when
        String url = userService.showProfileImage(mockUser);

        // then
        assertEquals("https://s3/oldImg", url);
    }

    @Test
    @DisplayName("getCurrentUser - 시큐리티 컨텍스트에서 사용자 이름 가져와 조회")
    void getCurrentUser() {
        // given
        String username = "testUser";
        // Suppose SecurityContextHolder has "testUser"
        given(userRepository.findByName(username)).willReturn(Optional.of(mockUser));

        // when
        User user = userService.getCurrentUser();

        // then
        assertNotNull(user);
        assertEquals("testUser", user.getName());
    }

    @Test
    @DisplayName("save - 단순 유저 저장")
    void save() {
        // when
        userService.save(mockUser);

        // then
        then(userRepository).should(times(1)).save(mockUser);
    }

    @Test
    @DisplayName("getMainInfo - 메인 페이지 정보 조회")
    void getMainInfo() {
        // given
        Pet pet = mock(Pet.class);
        mockUser.setPetList(Collections.singletonList(pet));
        given(userRepository.findByName("testUser")).willReturn(Optional.of(mockUser));

        // Mock RecordService & RemindService checks
        given(remindService.checkTodayRemind(pet)).willReturn(true);
        given(recordService.checkTodayRecord(pet)).willReturn(false);

        AiPost aiPost = mock(AiPost.class);
        given(aiPostService.getRecentAiPosts()).willReturn(List.of(aiPost));

        // when
        var mainInfo = userService.getMainInfo("testUser");

        // then
        assertNotNull(mainInfo);
        assertEquals("testUser", mainInfo.getName());
        assertFalse(mainInfo.getPetList().isEmpty());
        then(aiPostService).should().getRecentAiPosts();
    }
}
