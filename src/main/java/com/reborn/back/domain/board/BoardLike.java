package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boardLike")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blId", nullable = false)
    private Integer id;

    // FK: bId → Board(bId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bId", nullable = false)
    private Board board;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}