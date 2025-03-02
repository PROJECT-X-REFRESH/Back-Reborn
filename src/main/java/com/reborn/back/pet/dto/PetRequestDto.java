package com.reborn.back.pet.dto;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {
    private String name;
    private PetType petCase;
    private LocalDateTime birth;
    private LocalDateTime death;
    private PetColor color;
}