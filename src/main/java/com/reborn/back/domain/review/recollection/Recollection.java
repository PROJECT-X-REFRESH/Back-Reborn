package com.reborn.back.domain.review.recollection;


import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recollection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId", nullable = false, unique = true)
    private Pet pet;

    @OneToMany(mappedBy = "recollection")
    List<Record> records;

    @OneToMany(mappedBy = "recollection")
    List<Record> reminds;
}