package com.reborn.back.domain.contents.recollection;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reMind")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mindId")
    private Integer id;

    @Column(name = "mindName", length = 255)
    private String name;

    @Lob
    @Column(name = "mindContents", columnDefinition = "longtext")
    private String contents;

    @Column(name = "mindDate")
    private LocalDateTime date;

    // FK: pId → Pet(pId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId", nullable = false)
    private Pet pet;
}
