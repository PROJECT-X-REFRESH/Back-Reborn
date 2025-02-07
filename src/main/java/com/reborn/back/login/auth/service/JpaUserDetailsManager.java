package com.reborn.back.login.auth.service;


import com.reborn.back.domain.user.SocialAccount;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.login.auth.jwt.CustomUserDetails;
import com.reborn.back.login.repository.SocialAccountRepository;
import com.reborn.back.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    // UserDetailsManager의 구현체로 만들면, Spring Security Filter에서 사용자 정보 회수에 활요할 수 있음
    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SocialAccountRepository socialAccountRepository
    ) {
        this.userRepository = userRepository;
        this.socialAccountRepository = socialAccountRepository;
    }

    // 실제로 Spring Security 내부에서 사용하는 반드시 구현해야 정상동작을 기대할 수 있는 메소드
    // 사용자 이름으로 사용자 정보를 로드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("유저 이름 로드 중: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("유저 정보 없음: {}", username);
                    return new GeneralException(ErrorCode.USER_NOT_FOUND);
                });
        SocialAccount socialAccount = (SocialAccount) socialAccountRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        return CustomUserDetails.fromEntity(user, socialAccount);
    }

    @Override
    // 새로운 사용자 생성
    public void createUser(UserDetails user) {
        log.info("사용자 생성 시도 중: {}", user.getUsername());
        if (userExists(user.getUsername())) { // 사용자 존재 여부 확인.
            log.warn("사용자 이미 존재: {}", user.getUsername());
            throw new GeneralException(ErrorCode.ALREADY_USED_NICKNAME); // 닉네임 중복 예외 발생.
        }
        try {
            User newUser = userRepository.save(((CustomUserDetails) user).toEntity()); // 사용자 정보 저장.
            CustomUserDetails details = (CustomUserDetails) user;
            SocialAccount newSocialAccount = new SocialAccount();
            newSocialAccount.setProvider(details.getProvider());
            newSocialAccount.setProviderUserId(details.getUsername());
            newSocialAccount.setUser(newUser);
            socialAccountRepository.save(newSocialAccount);
            log.info("사용자 생성: {}", user.getUsername());
        } catch (ClassCastException e) {
            log.error("UserDetails ->CustomUserDetails 변환 실패: {}", user.getUsername(), e);
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    // 사용자 이름으로 존재 여부 체크
    public boolean userExists(String username) {
        log.info("사용자 존재 여부: {}", username);
        return userRepository.existsByUsername(username);
    }

    // 사용자 정보 업데이트
    // 구현 해야됨
    @Override
    public void updateUser(UserDetails user) {
        log.error("Update user functionality is not supported yet for user: {}", user.getUsername());
        throw new UnsupportedOperationException("Update user functionality is not implemented.");
    }

    // 사용자 정보 삭제
    // 구현 해야함
    @Override
    public void deleteUser(String username) {
        log.error("Delete user functionality is not supported yet for username: {}", username);
        throw new UnsupportedOperationException("Delete user functionality is not implemented.");
    }

    // 사용자 비번 변경
    // 구현 해야 함
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.error("Change password functionality is not supported yet.");
        throw new UnsupportedOperationException("Change password functionality is not implemented.");
    }
}
