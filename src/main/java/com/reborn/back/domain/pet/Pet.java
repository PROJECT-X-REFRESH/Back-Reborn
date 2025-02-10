package com.reborn.back.domain.pet;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pId")
    private Integer id;

    @Column(name = "pName", length = 255)
    private String name;

    // pCase를 PetType enum으로 매핑 (예: DOG, CAT)
    @Enumerated(EnumType.STRING)
    @Column(name = "pCase")
    private PetType petCase;

    @Column(name = "pBirth")
    private LocalDateTime birth;

    @Column(name = "pDeath")
    private LocalDateTime death;

    @Enumerated(EnumType.STRING)
    @Column(name = "pColor")
    private PetColor color;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}