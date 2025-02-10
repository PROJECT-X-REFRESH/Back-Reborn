package com.reborn.back.domain.user;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nId")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nType")
    private NoticeType type;

    @Column(name = "nRead")
    private Boolean read;

    // FK: uid → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user;
}