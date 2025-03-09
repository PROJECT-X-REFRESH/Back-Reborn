package com.reborn.back.login.auth.utils;

import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.login.auth.dto.JwtDto;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.auth.jwt.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;


// OAuth2 로그인 성공 핸들러
// JWT 토큰 생성&리프레시 토큰 저장 -> 클라이언트 리다이렉트
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RedisUtil redisUtil;
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsManager userDetailsManager;
    // 리다이렉트할 기본 URL
    @Value("${oauth2.redirect-url}")
    private String baseRedirectUrl;

    // 성공 시 처리
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        try {
            // OAuth2UserServiceImpl -> 사용자 정보 추출
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String providerId = oAuth2User.getAttribute("id");
            String email = oAuth2User.getAttribute("email");
            String provider = oAuth2User.getAttribute("provider");
            String username = String.format("{%s}%s", provider,oAuth2User.getAttribute("name"));
            String providerAccessToken = oAuth2User.getAttribute("oauth2AccessToken");
            LocalDateTime providerExpiresAt = oAuth2User.getAttribute("oauth2ExpiresAt");

            // 새로운 사용자를 데이터베이스에 등록
            if (!userDetailsManager.userExists(username)) {
                log.info("신규 사용자 생성: {}", username);
                CustomUserDetails newUser = CustomUserDetails.builder()
                        .providerId(providerId)
                        .username(username)
                        .email(email)
                        .provider(provider)
                        .accessToken(providerAccessToken)
                        .expireDate(providerExpiresAt)
                        .build();
                userDetailsManager.createUser(newUser);
            }

            // JWT 생성
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
            JwtDto jwt = tokenUtils.generateToken(userDetails);
            log.info("AccessToken: {}", jwt.getAccessToken());
            log.info("RefreshToken: {}", jwt.getRefreshToken());

            // Refresh Token 저장
            saveRefreshToken(jwt, username);

            // 리다이렉트 URL 생성
            String redirectUrl = String.format(
                    "%s?access-token=%s&refresh-token=%s",
                    baseRedirectUrl,
                    jwt.getAccessToken(),
                    jwt.getRefreshToken()
            );
            // 클라이언트 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 인증 성공 후 처리 중 오류 발생.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 인증 실패");
        }
    }

    // Refresh Token 저장 로직
    private void saveRefreshToken(JwtDto jwt, String username) {
        Claims refreshTokenClaims = tokenUtils.parseClaims(jwt.getRefreshToken());
        long validPeriod = refreshTokenClaims.getExpiration().toInstant().getEpochSecond()
                - refreshTokenClaims.getIssuedAt().toInstant().getEpochSecond();
        // 기존 저장된 Refresh Token 조회
        String existingToken = redisUtil.getData("username"+username);
        // 기존 값 로그 출력
        log.info("현재 저장된 Refresh Token (기존): {}", existingToken);
        // 기존 값이 다르면 새로 저장 (변경 확인 목적)
        if (existingToken != null && !existingToken.equals(jwt.getRefreshToken())) {
            log.info("🔄 Refresh Token 변경됨! 기존: {}, 새로운: {}", existingToken, jwt.getRefreshToken());
        }
        // Redis에 새 토큰 저장
        redisUtil.setDataExpire("username"+username, jwt.getRefreshToken(), validPeriod);
        // 저장 후 다시 확인
        log.info("✅ 저장된 Refresh Token (새로운): {}", redisUtil.getData("username"+username));
    }
}