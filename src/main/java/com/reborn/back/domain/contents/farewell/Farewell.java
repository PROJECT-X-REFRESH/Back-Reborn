package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Farewell")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farewell extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fId")
    private Integer id;

    @Column(name = "fStep")
    private Integer step;

    // FK: pId → Pet(pId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId", nullable = false)
    private Pet pet;
}
