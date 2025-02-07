package com.reborn.back.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String provider;
    // 소셜에서 사용하는 유저 식별자
    @Column(nullable = false)
    private String providerUserId;
    @Column(nullable = false)
    private String accessToken;
    @Column(nullable = false)
    private String refreshToken;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
