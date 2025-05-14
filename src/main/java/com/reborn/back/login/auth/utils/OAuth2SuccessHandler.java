package com.reborn.back.login.auth.utils;

import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedisUtil redisUtil;
    private final UserDetailsManager userDetailsManager;

    @Value("${spring.oauth2.redirect-url}")
    private String baseRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        try {
            log.info("✅ OAuth2 로그인 성공 - 사용자 인증 객체 수신");

            // 사용자 정보 추출
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String providerId = oAuth2User.getAttribute("id");
            String email = oAuth2User.getAttribute("email");
            String provider = oAuth2User.getAttribute("provider");
            String username = String.format("{%s}%s", provider, oAuth2User.getAttribute("name"));
            String providerAccessToken = oAuth2User.getAttribute("oauth2AccessToken");
            LocalDateTime providerExpiresAt = oAuth2User.getAttribute("oauth2ExpiresAt");

            log.info("🔍 사용자 정보 추출 완료: username={}, email={}, provider={}", username, email, provider);

            // 신규 유저 여부 확인
            boolean isNewUser = false;
            if (!userDetailsManager.userExists(username)) {
                isNewUser = true;
                log.info("🆕 신규 사용자 등록 시작: {}", username);

                CustomUserDetails newUser = CustomUserDetails.builder()
                        .providerId(providerId)
                        .username(username)
                        .email(email)
                        .provider(provider)
                        .accessToken(providerAccessToken)
                        .expireDate(providerExpiresAt)
                        .build();

                userDetailsManager.createUser(newUser);
                log.info("✅ 신규 사용자 등록 완료: {}", username);
            } else {
                log.info("👤 기존 사용자 로그인: {}", username);
            }

            // Redis에 authCode 저장
            String redisValue = username + ":" + (isNewUser ? "newUser" : "wasUser");
            String authCode = UUID.randomUUID().toString();
            redisUtil.setDataExpire("randomCode" + authCode, redisValue, 300);

            log.info("🧠 Redis에 인증 코드 저장 완료: key=randomCode{}, value={}", authCode, redisValue);

            // 앱으로 리디렉트할 딥링크 구성
            String redirectUrl = String.format("%s?code=%s", baseRedirectUrl, authCode);
            log.info("🚀 앱으로 리디렉트 시작 → {}", redirectUrl);

            // 실제 리디렉트
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("❌ OAuth2 인증 성공 후 처리 중 예외 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 인증 실패");
        }
    }
}