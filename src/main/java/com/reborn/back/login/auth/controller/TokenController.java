package com.reborn.back.login.auth.controller;

import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.dto.UserRequestDto;
import com.reborn.back.login.mapper.UserConverter;
import com.reborn.back.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "토큰", description = "access token 관련 api 입니다.")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    // 프론트엔드로 토큰 반환
    // 클라이언트->유저 정보->회원 가입 or JWT 생성 -> return
    // 로그인이 시작되는 부분이라고 볼 수 있음
    @Operation(summary = "토큰 반환", description = "프론트에게 유저 정보 받아 토큰 반환하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2011", description = "회원가입 & 로그인 성공"),
    })
    @PostMapping("/generate")
    public ApiResponse<JwtDto> tokenToFront(
            @RequestBody UserRequestDto userReqDto
    ) {
        // 1. 받은 email로 회원 여부 확인
        boolean isMember = userService.checkMemberByEmail(userReqDto.getEmail());
        User user;
        String signIn;
        if (isMember) { // 기존 회원 처리
            // 회원 정보 조회
            user = userService.findByEmail(userReqDto.getEmail());
            // 3. 회원 상태 여부를 저장 (신규 회원 여부)
            signIn = "wasUser";
            // FCM 토큰 저장
            userService.saveFcmToken(user, userReqDto.getDeviceToken());
        } else { // 신규 회원 처리
            // 회원가입 처리
            user = userService.createUser(userReqDto);
            // 신규 회원 상태 업데이트
            signIn = "newUser";
        }
        // JWT 생성
        JwtDto jwt = userService.jwtMakeSave(userReqDto.getUsername());
        String accessToken = jwt.getAccessToken();
        String refreshToken = jwt.getRefreshToken();
        // 4. 응답 데이터 반환
        return ApiResponse.onSuccess(SuccessCode.USER_LOGIN_SUCCESS, UserConverter.jwtDto(accessToken, refreshToken, signIn));
    }

    // 로컬
    @Operation(summary = "토큰 반환", description = "로컬에서 로그인했을때 토큰 반환하는 메서드입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "토큰 반환 Success"),
    })
    @GetMapping("/local")
    public ResponseEntity<Map<String, String>> tokenPage(
            @RequestParam(name = "access-token") String accessToken,
            @RequestParam(name = "refresh-token") String refreshToken
    ) {
        // 결과 데이터를 Map에 담아 반환
        Map<String, String> responseData = new HashMap<>();
        responseData.put("accessToken", accessToken);
        responseData.put("refreshToken", refreshToken);

        // JSON 형태로 응답
        return ResponseEntity.ok(responseData);
    }
}