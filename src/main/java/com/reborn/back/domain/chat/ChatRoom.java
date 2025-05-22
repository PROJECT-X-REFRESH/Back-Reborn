package com.reborn.back.domain.chat;

import com.reborn.back.domain.entity.BaseEntity;
import com.reborn.back.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "crLastTime")
    private LocalDateTime lastTime;

    // FK: crTo → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crTo", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User toUser;

    // FK: crFrom → User(uid)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crFrom", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User fromUser;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messageList = new ArrayList<>();
}