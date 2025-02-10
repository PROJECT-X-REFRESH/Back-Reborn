package com.reborn.back.domain.comment;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commentLike")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clId", nullable = false)
    private Integer id;

    // FK: cId → Comment(cId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cId", nullable = false)
    private Comment comment;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}
