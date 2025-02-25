package com.reborn.back.domain.aiPost;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aiPostView")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiPostView extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apvId", nullable = false)
    private Integer id;

    // FK: bId → Board(bId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apId", nullable = false)
    private AiPost aiPost;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}
