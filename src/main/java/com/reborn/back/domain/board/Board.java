package com.reborn.back.domain.board;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.BoardType;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Lob
    @Column(name = "bContent", columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(name = "bAttachImg", length = 255)
    private String attachImg;

    @Column(name = "bCommentCount", nullable = false)
    private Integer commentCount;

    @Column(name = "bviewCount", nullable = false)
    private Integer viewCount;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();

}