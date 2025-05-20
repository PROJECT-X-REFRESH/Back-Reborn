package com.reborn.back.chat.service;

import com.reborn.back.chat.dto.*;
import com.reborn.back.chat.repository.ChatMessageRepository;
import com.reborn.back.chat.repository.ChatRoomRepository;
import com.reborn.back.domain.chat.*;
import com.reborn.back.domain.user.User;
import com.reborn.back.login.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository roomRepo;
    private final ChatMessageRepository msgRepo;
    private final UserRepository userRepo;

    /* ========== 0) 목록 ========== */
    @Transactional(readOnly = true)
    public List<ChatRoomDto.RoomList> getRoomList(User me, int offset, int size) {
        Pageable page = PageRequest.of(offset / size, size, Sort.by(Sort.Direction.DESC, "lastTime"));
        return roomRepo.findRoomsByParticipant(me, page)
                .stream()
                .map(this::toRoomListDto)
                .toList();
    }

    /* ========== 1) handshake ========== */
    @Transactional
    public ChatRoomDto.RoomList handshake(User me, Long partnerId) {
        User partner = userRepo.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("상대방을 찾을 수 없습니다."));

        // 양방향 모두 조회
        ChatRoom room = roomRepo.findBetweenUsers(me.getId(), partnerId)
                .orElseGet(() -> createRoom(me, partner));

        return toRoomListDto(room);
    }

    /* ========== 2) 채팅 세부조회 ========== */
    @Transactional(readOnly = true)
    public List<ChatResDto.MessageResponse> getChatDetail(User me, Integer chatId,
                                                          int offset, int size) {

        ChatRoom room = authorizeAndGetRoom(chatId, me);

        Pageable page = PageRequest.of(offset / size, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return msgRepo.findByChatRoom(room, page)
                .map(this::toMsgDto)
                .getContent();
    }

    /* ========== 3) 메시지 저장 & 브로드캐스트 ========== */
    @Transactional
    public ChatResDto.MessageResponse writeMessage(User me,
                                                   Integer chatId,
                                                   String text) {

        ChatRoom room = authorizeAndGetRoom(chatId, me);

        ChatMessage entity = ChatMessage.builder()
                .chatRoom(room)
                .isFrom(room.getFromUser().getId().equals(me.getId())) // from → true
                .text(text)
                .build();

        msgRepo.save(entity);

        // 방 메타정보 갱신
        room.setLastTime(LocalDateTime.now());
        roomRepo.save(room);

        return toMsgDto(entity);
    }

    /* ========== 4) 방 나가기 ========== */
    @Transactional
    public void leaveRoom(User me, Integer chatId) {
        ChatRoom room = authorizeAndGetRoom(chatId, me);

        // 누가 나갔는지 상태 업데이트
        if (room.getFromUser().equals(me)) room.setStatus(ChatRoomStatus.FROM_LEFT);
        else                               room.setStatus(ChatRoomStatus.TO_LEFT);

        if (room.getStatus() == ChatRoomStatus.TO_LEFT && room.getStatus() == ChatRoomStatus.FROM_LEFT) {
            room.setStatus(ChatRoomStatus.BOTH_LEFT);
        }

        roomRepo.save(room);
    }

    /* ====== 내부 유틸 ====== */
    private ChatRoom createRoom(User from, User to) {
        ChatRoom room = ChatRoom.builder()
                .fromUser(from)
                .toUser(to)
                .status(ChatRoomStatus.NORMAL)
                .lastTime(LocalDateTime.now())
                .build();
        return roomRepo.save(room);
    }

    private ChatRoom authorizeAndGetRoom(Integer id, User me) {
        ChatRoom room = roomRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 없습니다."));
        boolean participant = room.getFromUser().equals(me) || room.getToUser().equals(me);
        if (!participant) throw new IllegalArgumentException("권한이 없습니다.");
        return room;
    }

    /* ====== DTO 변환 ====== */
    private ChatRoomDto.RoomList toRoomListDto(ChatRoom r) {
        User partner = r.getFromUser();
        if (partner.getId().equals(r.getFromUser().getId())) partner = r.getToUser();

        return ChatRoomDto.RoomList.builder()
                .roomId(r.getId())
                .partnerUserId(partner.getId())
                .partnerNickname(partner.getNickname())
                .lastMsg(r.getMessageList().isEmpty() ? "" :
                        r.getMessageList().get(r.getMessageList().size() - 1).getText())
                .lastTime(r.getLastTime())
                .build();
    }

    private ChatResDto.MessageResponse toMsgDto(ChatMessage m) {
        return ChatResDto.MessageResponse.builder()
                .messageId(m.getId())
                .isFrom(m.getIsFrom())
                .text(m.getText())
                .sentAt(m.getCreatedAt())
                .build();
    }
}