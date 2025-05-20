package com.reborn.back.domain.review.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String ribbon;

    @Lob
    @Column(name = "birthPetPost", columnDefinition = "longtext")
    private String petPost;

    @Column(name = "birthOutro")
    private Boolean outro;

    // FK: fId → Farewell(fId)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farewell farewell;
}
