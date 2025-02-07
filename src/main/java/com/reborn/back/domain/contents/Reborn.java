package com.reborn.back.domain.contents;

import com.reborn.back.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reborn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column
    private Integer date;

    @Column
    private String rebornContent;

    @Enumerated(EnumType.STRING)
    @Column
    private RebornType rebornType = RebornType.BLACK;

    @Column
    private Boolean pat;

    @Column
    private Boolean feed;

    @Column
    private Boolean wash;

    @Column
    private Boolean clothe;
}
