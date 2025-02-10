package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reCord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cordId")
    private Integer id;

    @Column(name = "cordTitle", length = 255)
    private String title;

    @Lob
    @Column(name = "cordContent", columnDefinition = "longtext")
    private String content;

    @Column(name = "cordDate")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "cordWeather")
    private WeatherType weather;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "pos", column = @Column(name = "cordPos")),
        @AttributeOverride(name = "neg", column = @Column(name = "cordNeg")),
        @AttributeOverride(name = "neu", column = @Column(name = "cordNeu")),
        @AttributeOverride(name = "state", column = @Column(name = "cordState")),
    })
    private Emotion emotion;

    // FK: pId → Pet(pId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId", nullable = false)
    private Pet pet;
}
