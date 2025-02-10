package com.reborn.back.domain.board;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boardBookmark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bbId", nullable = false)
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