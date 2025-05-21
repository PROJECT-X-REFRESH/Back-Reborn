    package com.reborn.back.chat.repository;

    import com.reborn.back.domain.chat.ChatMessage;
    import com.reborn.back.domain.chat.ChatRoom;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
        Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
    }

