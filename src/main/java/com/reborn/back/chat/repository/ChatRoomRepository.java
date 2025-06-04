package com.reborn.back.chat.repository;

import com.reborn.back.domain.chat.ChatRoom;
import com.reborn.back.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    @Query("""
            SELECT r FROM ChatRoom r
            WHERE (r.fromUser = :user OR r.toUser = :user)
              AND r.status <> 'BOTH_LEFT'
            """)
    Page<ChatRoom> findRoomsByParticipant(@Param("user") User user, Pageable pageable);

    @Query("""
            SELECT r FROM ChatRoom r
            WHERE ((r.fromUser.uid = :uid1 AND r.toUser.uid = :uid2)
                OR (r.fromUser.uid = :uid2 AND r.toUser.uid = :uid1))
            AND r.status <> 'BOTH_LEFT'
            """)
    Optional<ChatRoom> findBetweenUsers(@Param("uid1") String uid1,
                                        @Param("uid2") String uid2);

    @Query("""
            SELECT r FROM ChatRoom r
            WHERE ((r.fromUser.uid = :a AND r.toUser.uid = :b)
                OR (r.fromUser.uid = :b AND r.toUser.uid = :a))
            AND r.status <> 'BOTH_LEFT'
            ORDER BY r.id ASC
            LIMIT 1
             """)
    Optional<ChatRoom> findBetween(@Param("a") String a, @Param("b") String b);
}