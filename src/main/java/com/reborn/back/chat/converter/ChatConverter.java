package com.reborn.back.chat.converter;

import com.reborn.back.chat.dto.ChatDto;
import com.reborn.back.domain.chat.ChatMessage;
import com.reborn.back.domain.chat.ChatRoom;
import com.reborn.back.domain.chat.ChatRoomStatus;
import com.reborn.back.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatConverter {
    public static ChatDto.ChatRoomDto.RoomList toRoomListDto(ChatRoom r, User me) {
        User partner = r.getFromUser().equals(me) ? r.getToUser() : r.getFromUser();

        return ChatDto.ChatRoomDto.RoomList.builder()
                .roomId(r.getId())
                .partnerUserId(partner.getUid())
                .partnerNickname(partner.getName())
                .lastMsg(r.getMessageList().isEmpty() ? "" :
                        r.getMessageList().get(r.getMessageList().size() - 1).getText())
                .lastTime(r.getLastTime())
                .build();
    }

    public static ChatDto.ChatResDto toMsgDto(ChatMessage m, User me) {
        boolean iAmFrom = m.getChatRoom().getFromUser().equals(me);
        boolean mine = (iAmFrom && m.getIsFrom()) || (!iAmFrom && !m.getIsFrom());

        return ChatDto.ChatResDto.builder()
                .messageId(m.getId())
                .mine(mine)
                .text(m.getText())
                .sentAt(m.getCreatedAt())
                .build();
    }

    public static ChatRoom toNewChatRoom(User from, User to) {
        return ChatRoom.builder()
                .fromUser(from)
                .toUser(to)
                .status(ChatRoomStatus.NORMAL)
                .lastTime(LocalDateTime.now())
                .messageList(new ArrayList<>())
                .build();
    }

    public static ChatMessage toChatMessage(User sender, ChatRoom room, String text) {
        boolean isFrom = room.getFromUser().getUid().equals(sender.getUid());
        return ChatMessage.builder()
                .chatRoom(room)
                .isFrom(isFrom)
                .text(text)
                .build();
    }
}
