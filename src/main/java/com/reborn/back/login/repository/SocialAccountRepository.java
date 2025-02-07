package com.reborn.back.login.repository;

import com.reborn.back.domain.user.SocialAccount;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    // 이 user_id와 provider가 같은 소셜 계정을 찾기
    Optional<SocialAccount> findByUserIdAndProvider(Long userId, String provider);

    // provider + providerUserId 로 소셜 계정 찾기 (로그인 시 사용)
    Optional<SocialAccount> findByProviderAndProviderUserId(String provider, String providerUserId);

    Optional<Object> findByUser(User user);
}