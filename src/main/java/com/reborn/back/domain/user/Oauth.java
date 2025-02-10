package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oauth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authId")
    private Integer id;

    @Column(nullable = false)
    private String providerUserId;

    @Column(name = "accessToken", length = 255)
    private String accessToken;

    @Column(name = "expireDate")
    private LocalDateTime expireDate;

    @Column(name = "refreshToken", length = 255)
    private String refreshToken;

    @Column(name = "provider", length = 20)
    private String provider;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}