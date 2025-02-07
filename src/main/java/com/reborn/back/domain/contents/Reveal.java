package com.reborn.back.domain.contents;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.PickEmotion;
import com.reborn.back.domain.entity.ResultEmotion;
import com.reborn.back.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reveal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column
    private Integer date;

    @Column
    private String diaryContent;

    @Enumerated(EnumType.STRING)
    @Column
    private PickEmotion pickEmotion;

    @Enumerated(EnumType.STRING)
    @Column
    private ResultEmotion resultEmotion;

    @Column
    private Boolean pat;

    @Column
    private Boolean feed;

    @Column
    private Boolean walk;

    @Column
    private Boolean snack;
}
