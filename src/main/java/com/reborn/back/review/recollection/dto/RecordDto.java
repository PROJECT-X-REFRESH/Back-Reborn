package com.reborn.back.review.recollection.dto;

import com.reborn.back.domain.entity.Emotion;
import com.reborn.back.domain.pet.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {
    private Integer id;
    private String title;
    private String content;
    private Emotion emotion;
    private LocalDateTime createdAt;
}
