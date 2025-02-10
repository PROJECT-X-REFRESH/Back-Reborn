package com.reborn.back.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatMessage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmId", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "cmText", columnDefinition = "longtext", nullable = false)
    private String text;

    @Column(name = "cmIsFrom", nullable = false)
    private Boolean isFrom;

    @Column(name = "cmRead", nullable = false)
    private Boolean read;

    @Column(name = "cmTime", nullable = false)
    private LocalDateTime time;

    @Column(name = "cmSignture", length = 255, nullable = false)
    private String signature;

    // FK: cmRoomId → ChatRoom(crId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmRoomId", nullable = false)
    private ChatRoom chatRoom;
}