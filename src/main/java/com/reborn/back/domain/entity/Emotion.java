package com.reborn.back.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Emotion {

    @Column(name = "Pos")
    private Float pos;

    @Column(name = "Neg")
    private Float neg;

    @Column(name = "Neu")
    private Float neu;

    @Enumerated(EnumType.STRING)
    @Column(name = "State")
    private EmotionState state;
}