package com.reborn.back.domain.diary;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.PickEmotion;
import com.reborn.back.domain.entity.ResultEmotion;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rediary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rediaryId;

    @Column(length = 20, nullable = false)
    private String rediaryWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String rediaryTitle;

    @Column(nullable = false)
    private LocalDate rediaryCreatedAt;

    @Column(nullable = false)
    private String rediaryContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickEmotion pickEmotion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultEmotion resultEmotion;

}
