package com.reborn.back.domain.aiPost;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aiPost")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apId", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "apContent", columnDefinition = "longtext", nullable = false)
    private String content;

    @Column(name = "apAttachImg", length = 255)
    private String attachImg;

    @Column(name = "apUrl", columnDefinition = "text", nullable = false)
    private String url;

    @Column(name = "apTitle", columnDefinition = "text", nullable = false)
    private String title;

    @OneToMany(mappedBy = "aiPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiPostLike> aiPostLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "aiPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiPostBookmark> aiPostBookmarkList = new ArrayList<>();
}