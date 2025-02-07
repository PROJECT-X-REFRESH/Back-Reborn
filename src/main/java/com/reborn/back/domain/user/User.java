package com.reborn.back.domain.user;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.board.BoardBookmark;
import com.reborn.back.domain.board.BoardLike;
import com.reborn.back.domain.comment.Comment;
import com.reborn.back.domain.diary.Rediary;
import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.pet.Pet;
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
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String phoneNum;

    @Column(nullable = true)
    private Long contentPetId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String backgroundImage;

    @Column(nullable = true)
    private String deviceToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Pet> petList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BoardBookmark> boardBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Rediary> rediaryList = new ArrayList<>();
}
