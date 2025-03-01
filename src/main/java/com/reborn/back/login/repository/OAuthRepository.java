package com.reborn.back.login.repository;

import com.reborn.back.domain.user.OAuth;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<OAuth, Long> {

    // 이 user_id와 provider가 같은 소셜 계정을 찾기
    Optional<OAuth> findByIdAndProvider(Integer id, String provider);

    // provider + providerUserId 로 소셜 계정 찾기 (로그인 시 사용)
    Optional<OAuth> findByProviderAndProviderUserId(String provider, String providerUserId);

    Optional<Object> findByUser(User user);

    boolean existsByProviderUserId(String id);
}