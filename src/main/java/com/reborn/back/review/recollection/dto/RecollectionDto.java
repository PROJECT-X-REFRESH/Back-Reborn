package com.reborn.back.review.recollection.dto;

import com.reborn.back.domain.entity.EmotionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecollectionDto {
    LocalDate day;
    boolean didRemid;
    boolean didRecord;
    EmotionState recordEmotion;
}
