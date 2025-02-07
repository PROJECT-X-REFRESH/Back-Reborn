package com.reborn.back.domain.board;

import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.BoardType;
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
@Table(name = "board")
public class Board extends BaseEntity {
    /*
    1. EmotionShareBoard
    2. ActivityShareBoard
    3. ChatShareBoard
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, columnDefinition = "varchar(45) default 'ACTIVE'", nullable = false)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20, nullable = false)
    private String boardWriter;

    @Column
    private Long likeCount;

    @Column
    private Long commentCount;

    @Column(length = 5000, nullable = false)
    private String boardContent;

    @Column
    private String boardImage;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardBookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLike> likeList = new ArrayList<>();

    public void updateLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public void updateCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    @PrePersist
    @PreUpdate
    private void validateBoardType() {
        if (this.boardType == null) {
            throw new IllegalArgumentException("BoardType을 선택하셔야 게시판이 생성됩니다.");
        }
    }

}