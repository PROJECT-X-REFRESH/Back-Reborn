package com.reborn.back.domain.chat;

import com.reborn.back.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    // FK: cmRoomId → ChatRoom(crId)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmRoomId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;
}