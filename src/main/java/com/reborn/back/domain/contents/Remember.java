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
public class Remember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column
    private Integer date;

    @Column
    private String rememberImage;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String imageDate;

    @Column
    private Boolean pat;

    @Column
    private Boolean feed;

    @Column
    private Boolean walk;

    @Column
    private Boolean snack;

    @Column
    private Boolean clean;

}
