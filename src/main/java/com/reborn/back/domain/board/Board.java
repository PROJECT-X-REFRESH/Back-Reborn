package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bId", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bCategory", nullable = false)
    private BoardType category;

    @Column(name = "bTitle", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "bContent", columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(name = "bAttachImg", length = 255)
    private String attachImg;

    @Column(name = "bDate", nullable = false)
    private LocalDateTime date;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}