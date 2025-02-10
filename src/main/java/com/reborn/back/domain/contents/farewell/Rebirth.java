package com.reborn.back.domain.contents.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reBirth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rebirth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "birthId")
    private Integer id;

    @Column(name = "birthWash")
    private Boolean wash;

    @Column(name = "birthDress")
    private Boolean dress;

    @Column(name = "birthRibbon")
    private Boolean ribbon;

    @Lob
    @Column(name = "birthPost", columnDefinition = "longtext")
    private String post;

    // FK: fId → Farewell(fId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    private Farewell farewell;
}
