package com.reborn.back.domain.chat;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatRoom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crId", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "crStatus", nullable = false)
    private ChatRoomStatus status;

    @Column(name = "crLastMsg", length = 255)
    private String lastMsg;

    @Column(name = "crLastTime")
    private LocalDateTime lastTime;

    @Column(name = "crToPubKey", length = 255)
    private String toPubKey;

    @Column(name = "crFromPubKey", length = 255)
    private String fromPubKey;

    @Column(name = "crToSKey", length = 255)
    private String toSKey;

    @Column(name = "crFromSKey", length = 255)
    private String fromSKey;

    // FK: crTo → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crTo", nullable = false)
    private User toUser;

    // FK: crFrom → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crFrom", nullable = false)
    private User fromUser;
}