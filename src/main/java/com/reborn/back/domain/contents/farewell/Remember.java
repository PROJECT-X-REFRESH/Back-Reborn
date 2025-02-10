package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reMember")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Integer id;

    @Column(name = "memberDay")
    private Integer day;

    @Column(name = "memberPet")
    private Boolean pet;

    @Column(name = "memberFeed")
    private Boolean feed;

    @Column(name = "memberSnack")
    private Boolean snack;

    @Column(name = "memberWalk")
    private Boolean walk;

    @Column(name = "memberUrl", length = 255)
    private String url;

    @Lob
    @Column(name = "memberContent", columnDefinition = "longtext")
    private String content;

    // FK: fId → Farewell(fId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    private Farewell farewell;
}