package com.reborn.back.chat.service;

import com.reborn.back.chat.converter.ChatConverter;
import com.reborn.back.chat.dto.ChatDto;
import com.reborn.back.chat.repository.ChatMessageRepository;
import com.reborn.back.chat.repository.ChatRoomRepository;
import com.reborn.back.domain.chat.ChatMessage;
import com.reborn.back.domain.chat.ChatRoom;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.reborn.back.domain.chat.ChatRoomStatus.FROM_LEFT;
import static com.reborn.back.domain.chat.ChatRoomStatus.TO_LEFT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository msgRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChatDto.ChatRoomDto.RoomList> getRoomList(User me, int offset, int size) {
        Pageable page = PageRequest.of(offset / size, size, Sort.by(Sort.Direction.DESC, "lastTime"));
        return chatRoomRepository.findRoomsByParticipant(me, page)
                .stream()
                .map(room -> ChatConverter.toRoomListDto(room, me))  // me 전달
                .toList();
    }

    @Transactional
    public ChatDto.ChatRoomDto.RoomList handshake(User me, String partnerUid) {
        User partner = userRepository.findByName(partnerUid)
                .orElseThrow(() ->
                        new EntityNotFoundException("상대방을 찾을 수 없습니다."));
        return chatRoomRepository.findBetweenUsers(me.getUid(), partnerUid)
                .map(r -> ChatConverter.toRoomListDto(r, me))
                .orElseGet(() -> tryCreateRoom(me, partner));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChatDto.ChatRoomDto.RoomList tryCreateRoom(User me, User partner) {
        try {
            ChatRoom created = createRoom(me, partner);
            return ChatConverter.toRoomListDto(created, me);
        } catch (DataIntegrityViolationException dup) {
            ChatRoom existing = chatRoomRepository.findBetweenUsers(
                            me.getUid(), partner.getUid())
                    .orElseThrow();
            return ChatConverter.toRoomListDto(existing, me);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatDto.ChatResDto> getChatDetail(User me, Integer chatId, int offset, int size) {
        ChatRoom room = authorizeAndGetRoom(chatId, me);
        Pageable page = PageRequest.of(offset / size, size,
                Sort.by(Sort.Direction.ASC, "createdAt"));
        return msgRepository.findByChatRoom(room, page)
                .map(msg -> ChatConverter.toMsgDto(msg, me))
                .getContent();
    }

    // 3) 메시지 저장 / 브로드캐스트
    @Transactional
    public ChatDto.ChatResDto writeMessage(User me, Integer chatId, String text) {

        ChatRoom room = authorizeAndGetRoom(chatId, me);
        ChatMessage entity = ChatConverter.toChatMessage(me, room, text);
        msgRepository.save(entity);
        room.setLastTime(LocalDateTime.now());
        chatRoomRepository.save(room);
        return ChatConverter.toMsgDto(entity, me);
    }

    // 4) 방 나가기
    @Transactional
    public void leaveRoom(User me, Integer chatId) {
        ChatRoom room = authorizeAndGetRoom(chatId, me);
        boolean meIsFrom = room.getFromUser().equals(me);
        switch (room.getStatus()) {
            case NORMAL -> {
                room.setStatus(meIsFrom ? FROM_LEFT : TO_LEFT);
                chatRoomRepository.save(room);
            }
            case FROM_LEFT -> {
                if (meIsFrom) throw new IllegalStateException("이미 방을 나갔습니다.");
                chatRoomRepository.delete(room);
            }
            case TO_LEFT -> {
                if (!meIsFrom) throw new IllegalStateException("이미 방을 나갔습니다.");
                chatRoomRepository.delete(room);
            }
            case BOTH_LEFT -> throw new IllegalStateException("이미 삭제된 방입니다.");
        }
    }

    @Transactional
    public ChatRoom createRoom(User from, User to) {
        ChatRoom room = ChatConverter.toNewChatRoom(from, to);
        return chatRoomRepository.save(room);
    }

    public ChatRoom authorizeAndGetRoom(int id, User me) {
        ChatRoom room = chatRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 없습니다."));
        String myId = me.getUid();
        String fromId = room.getFromUser().getUid();
        String toId = room.getToUser().getUid();

        if (!Objects.equals(myId, fromId) && !Objects.equals(myId, toId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return room;
    }
}