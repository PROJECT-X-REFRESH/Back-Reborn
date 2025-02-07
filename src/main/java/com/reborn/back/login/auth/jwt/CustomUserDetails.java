package com.reborn.back.login.auth.jwt;

import com.reborn.back.domain.user.SocialAccount;
import com.reborn.back.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// spring security에서의 사용자 인증 정보
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String provider;

    public static CustomUserDetails fromEntity(User entity, SocialAccount socialAccount) {
        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .provider(socialAccount.getProvider())
                .build();
    }

    // 권한 부여
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    // 비밀번호 반환
    // 자체 로그인이 있다면 필요하지만 소셜 로그인만 취급중이므로 고정값 설정
    @Override
    public String getPassword() {
        return "Password";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .nickname(this.nickname)
                .build();
    }


    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}