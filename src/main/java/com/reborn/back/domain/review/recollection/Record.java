package com.reborn.back.domain.review.recollection;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.Emotion;
import com.reborn.back.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

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

    @Embedded
    @AttributeOverride(
            name = "state",
            column = @Column(name = "cordState")
    )
    private Emotion emotion;

    // FK: pId → Pet(pId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recollection_id")
    private Recollection recollection;
}
