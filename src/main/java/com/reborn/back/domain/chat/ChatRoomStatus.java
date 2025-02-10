package com.reborn.back.domain.entity;

public enum ChatRoomStatus {
    NORMAL,    // 기본 (0)
    TO_LEFT,   // To가 나감 (1)
    FROM_LEFT, // From이 나감 (2)
    BOTH_LEFT  // 둘 다 나간 경우 (삭제)
}