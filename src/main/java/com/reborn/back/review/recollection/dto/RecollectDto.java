package com.reborn.back.review.recollection.dto;

import com.reborn.back.domain.entity.EmotionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class RecollectDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class RecollectionDto {
        LocalDate day;
        boolean didRemind;
        boolean didRecord;
        EmotionState recordEmotion;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TodayRecollctDto {
        Integer remindId;
        Integer recordId;
    }
}
