package com.reborn.back.domain.review.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.Emotion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "reVeal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reveal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vealMind")
    private Integer id;

    @Column(name = "vealFeed")
    private Boolean feed;

    @Column(name = "vealSnack")
    private Boolean snack;

    @Column(name = "vealWalk")
    private Boolean walk;

    @Lob
    @Column(name = "vealContents", columnDefinition = "longtext")
    private String contents;

    @Embedded
    @AttributeOverride(
            name = "state",
            column = @Column(name = "vealState")
    )
    private Emotion emotion;

    // FK: fId → Farewell(fId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farewell farewell;
}