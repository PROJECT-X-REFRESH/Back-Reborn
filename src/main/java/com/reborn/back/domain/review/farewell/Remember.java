package com.reborn.back.domain.review.farewell;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.entity.OrganizeType;
import com.reborn.back.domain.entity.PetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reMember")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Integer id;

    @Column(name = "memberDay")
    private Integer day;

    @Column(name = "memberFeed")
    private Boolean feed;

    @Column(name = "memberSnack")
    private Boolean snack;

    @Column(name = "memberWalk")
    private Boolean walk;

    @Column(name = "memberUrl", length = 255)
    private String url;

    @Lob
    @Column(name = "memberContent", columnDefinition = "longtext")
    private String content;

    @ElementCollection(targetClass = OrganizeType.class)
    @CollectionTable(name = "memberthing", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "thing")
    private Set<OrganizeType> things = new HashSet<>();


    // FK: fId → Farewell(fId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Farewell farewell;
}