package com.reborn.back.domain.contents.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.Emotion;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "vealDay")
    private Integer day;

    @Column(name = "vealPet")
    private Boolean pet;

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
    @AttributeOverrides({
            @AttributeOverride(name = "pos", column = @Column(name = "vealPos")),
            @AttributeOverride(name = "neg", column = @Column(name = "vealNeg")),
            @AttributeOverride(name = "neu", column = @Column(name = "vealNeu")),
            @AttributeOverride(name = "state", column = @Column(name = "vealState")),
    })
    private Emotion emotion;

    // FK: fId → Farewell(fId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    private Farewell farewell;
}