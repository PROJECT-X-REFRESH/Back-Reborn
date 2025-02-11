package com.reborn.back.domain.contents.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.pet.Pet;
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

    @OneToOne(mappedBy = "farewell", cascade = CascadeType.ALL, orphanRemoval = true)
    private Rebirth rebirth;

    @OneToOne(mappedBy = "farewell", cascade = CascadeType.ALL, orphanRemoval = true)
    private Recognize recognize;

    @OneToMany(mappedBy = "farewell", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Remember> rememberList = new ArrayList<>();

    @OneToMany(mappedBy = "farewell", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reveal> revealList = new ArrayList<>();
}
