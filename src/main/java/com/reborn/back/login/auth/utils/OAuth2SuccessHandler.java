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
import java.util.UUID;


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
            boolean isNewUser = false;

            if (!userDetailsManager.userExists(username)) {
                isNewUser = true; // 신규 유저
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
            String redisValue = username + ":" + (isNewUser ? "newUser" : "wasUser");
            String authCode = UUID.randomUUID().toString();
            redisUtil.setDataExpire("randomCode" + authCode, redisValue, 300);

            // 앱으로 리디렉트할 딥링크
            String redirectUrl = String.format("%s?code=%s", baseRedirectUrl, authCode);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 인증 성공 후 처리 중 오류 발생.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 인증 실패");
        }
    }

}