package com.reborn.back.domain.comment;

import com.reborn.back.domain.board.Board;
import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cId", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "cContents", columnDefinition = "longtext", nullable = false)
    private String contents;

    @Column(name = "cDate", nullable = false)
    private LocalDateTime date;

    @Column(name = "cIsDeleted", nullable = false)
    private Boolean isDeleted;

    // FK: bId → Board(bId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bId", nullable = false)
    private Board board;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

}