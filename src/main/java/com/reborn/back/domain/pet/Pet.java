package com.reborn.back.domain.pet;

import com.reborn.back.domain.contents.Reborn;
import com.reborn.back.domain.contents.Remember;
import com.reborn.back.domain.contents.Remind;
import com.reborn.back.domain.contents.Reveal;
import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import com.reborn.back.domain.entity.RebornType;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String petName;

    @Column(nullable = false)
    private String anniversary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = false)
    private String detailPetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetColor petColor;

    @Column(nullable = false)
    private String progressState;

    @Column
    private Integer rebornDate;

    @Enumerated(EnumType.STRING)
    @Column
    private RebornType rebornType;

    // 나의 반려견 15일 컨텐츠 기록 열람시 필요
    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Remind> remindList = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reveal> revealList = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Remember> rememberList = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reborn> rebornList = new ArrayList<>();

}