package com.reborn.back.domain.review.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "reCognize")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recognize extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cogId")
    private Integer id;

    @Column(name = "cogScore")
    private Integer score;

    // FK: fId → Farewell(fId)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farewell farewell;
}
